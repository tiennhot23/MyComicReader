package com.e.mycomicreader.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.e.mycomicreader.Common.AsyncTaskResponse;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ChapterAdapter;
import com.e.mycomicreader.entity.MarkedChapter;
import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.MarkedChapterViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

public class ChapterActivity extends AppCompatActivity implements AsyncTaskResponse{
    private CompositeDisposable compositeDisposable;
    IComicAPI iComicAPI;
    private ViewPager2 viewPager;
    private int position;
    private List<Chapter> chapter_list;
    private String chapter_endpoint, endpoint;
    private FloatingActionButton more, next, prev, beenhere;
    private Boolean isMoreClicked = true;
    private Boolean isMarkedChapterExisted = false;

    MarkedChapterViewModel markedChapterViewModel;
    BackGroundTask backGroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        position = getIntent().getIntExtra("position", 0);
        chapter_list = (List<Chapter>) getIntent().getSerializableExtra("chapter_list");
        chapter_endpoint = chapter_list.get(position).chapter_endpoint;
        viewPager = findViewById(R.id.view_pager);
        more = findViewById(R.id.more);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        beenhere = findViewById(R.id.beenhere);
        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        markedChapterViewModel = new ViewModelProvider(this).get(MarkedChapterViewModel.class);

        fetchChapter();


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleButtons();
                setAnimateButtons();
                isMoreClicked = !isMoreClicked;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position - 1 < 0){
                    return;
                }
                Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
                intent.putExtra("position", position - 1);
                intent.putExtra("chapter_list", (Serializable) chapter_list);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position + 1 == chapter_list.size()){
                    return;
                }
                Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
                intent.putExtra("position", position + 1);
                intent.putExtra("chapter_list", (Serializable) chapter_list);
                startActivity(intent);
                finish();
            }
        });

        beenhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMarkedChapterExisted){
                    markedChapterViewModel.update(new MarkedChapter(DetailComicActivity.endpoint, chapter_endpoint));
                }else{
                    markedChapterViewModel.insert(new MarkedChapter(DetailComicActivity.endpoint, chapter_endpoint));
                }
                Toast.makeText(ChapterActivity.this, "Đã đánh dấu", Toast.LENGTH_SHORT).show();
            }
        });

        backGroundTask = new BackGroundTask(this, markedChapterViewModel, DetailComicActivity.endpoint);
        backGroundTask.res = this;
        backGroundTask.execute();
    }

    private void fetchChapter() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getChapter(chapter_endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Chapter>>() {
                    @Override
                    public void accept(List<Chapter> chapter) throws Exception {
                        viewPager.setAdapter(new ChapterAdapter(getApplication().getBaseContext(), chapter.get(0).chapter_image));
                        dialog.dismiss();
                    }
                }));
    }

    private void setVisibleButtons(){
        if(isMoreClicked){
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            beenhere.setVisibility(View.VISIBLE);
        }else{
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            beenhere.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimateButtons(){
        if(isMoreClicked){
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_out);
            next.startAnimation(anim);
            prev.startAnimation(anim);
            beenhere.startAnimation(anim);
        }else{
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_in);
            next.startAnimation(anim);
            prev.startAnimation(anim);
            beenhere.startAnimation(anim);
        }
    }

    @Override
    protected void onDestroy() {
        if(backGroundTask != null) backGroundTask.cancel(true);
        super.onDestroy();
    }

    @Override
    public void processFinish(String output) {
        if(output == null){
            isMarkedChapterExisted = false;
        }else{
            isMarkedChapterExisted = true;
        }
    }

    @Override
    public void downloadFinish(String output) {

    }

    private static class BackGroundTask extends AsyncTask<Void, Void, String> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private MarkedChapterViewModel markedChapterViewModel;
        private String endpoint;
        private AlertDialog dialog;
        private AsyncTaskResponse res = null;

        public BackGroundTask(Activity activity, MarkedChapterViewModel markedChapterViewModel, String endpoint) {
            weakActivity = new WeakReference<>(activity);
            this.markedChapterViewModel = markedChapterViewModel;
            this.endpoint = endpoint;
        }

        @Override
        protected void onPreExecute() {
            dialog = new SpotsDialog.Builder().setContext(weakActivity.get()).setMessage("Loading...").build();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            MarkedChapter markedChapter = markedChapterViewModel.findMarkedChapter(endpoint);
            if(markedChapter == null) return null;
            String chapter_endpoint = markedChapter.chapter_endpoint;
            return chapter_endpoint;
        }

        @Override
        protected void onPostExecute(String chapter_endpoint) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
            dialog.dismiss();
            res.processFinish(chapter_endpoint);
        }
    }
}