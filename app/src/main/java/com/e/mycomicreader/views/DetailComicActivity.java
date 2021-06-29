package com.e.mycomicreader.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.e.mycomicreader.Common.AsyncTaskResponse;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.entity.MarkedChapter;
import com.e.mycomicreader.fragments.MyBottomSheetFragement;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.models.MarkedChapterViewModel;
import com.squareup.picasso.Picasso;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

public class DetailComicActivity extends AppCompatActivity implements AsyncTaskResponse{

    public static String endpoint;
    private TextView title_comic, author, status, rating, updated_on, genre, view, desc, read, read_continue, chapter_list, btn_follow;
    private ImageView theme, thumb ,btn_go_back;
    private CompositeDisposable compositeDisposable;
    private Toast toast;
    private DetailComic detailComic = new DetailComic();
    private String chapter_endpoint;

    IComicAPI iComicAPI;
    MyBottomSheetFragement bottomSheetDialog;
    MarkedChapterViewModel markedChapterViewModel;
    BackGroundTask backGroundTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comic);

        endpoint = getIntent().getStringExtra("endpoint");

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();

        title_comic = findViewById(R.id.title_comic);
        author = findViewById(R.id.author);
        status = findViewById(R.id.status);
        rating = findViewById(R.id.rating);
        updated_on = findViewById(R.id.updated_on);
        genre = findViewById(R.id.genre);
        view = findViewById(R.id.view);
        desc = findViewById(R.id.desc);
        theme = findViewById(R.id.theme);
        thumb = findViewById(R.id.thumb);
        read = findViewById(R.id.read);
        read_continue = findViewById(R.id.read_continue);
        chapter_list = findViewById(R.id.chapter_list);
        btn_follow = findViewById(R.id.btn_follow);
        btn_go_back = findViewById(R.id.btn_go_back);

        markedChapterViewModel = new ViewModelProvider(this).get(MarkedChapterViewModel.class);

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=detailComic.chapter_list.size()-1;
                Intent intent = new Intent(DetailComicActivity.this, ChapterActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
                startActivity(intent);
            }
        });

        read_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=detailComic.chapter_list.size()-1;
                if(chapter_endpoint == null) return;
                for(int i=0 ;i<detailComic.chapter_list.size(); i++){
                    if(detailComic.chapter_list.get(i).chapter_endpoint.equals(chapter_endpoint)){
                        position = i;
                        break;
                    }
                }
                Intent intent = new Intent(DetailComicActivity.this, ChapterActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
                startActivity(intent);
            }
        });

        fetchDetailComic();

        chapter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toast != null) toast.cancel();
                toast = Toast.makeText(getBaseContext(),"Followed", Toast.LENGTH_SHORT);
                toast.show();
                MainActivity.followedComicViewModel.insert(new FollowedComic(endpoint));
                if(!MainActivity.isFollowed.containsKey(endpoint)){
                    MainActivity.isFollowed.put(endpoint, true);
                }
            }
        });

        backGroundTask = new BackGroundTask(this, markedChapterViewModel, endpoint);
        backGroundTask.res = this;
        backGroundTask.execute();
    }

    private void fetchDetailComic() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getDetailComic(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DetailComic>>() {
                    @Override
                    public void accept(List<DetailComic> detailComics) throws Exception {
                        detailComic = detailComics.get(0);
                        title_comic.setText(detailComics.get(0).title);
                        author.setText(detailComics.get(0).author);
                        status.setText(detailComics.get(0).status);
                        rating.setText(detailComics.get(0).rating);
                        updated_on.setText(detailComics.get(0).updated_on);
                        StringBuilder genrelist = new StringBuilder();
                        for(int i=0; i<detailComics.get(0).genre_list.size(); i++){
                            if(i==0)
                                genrelist.append(detailComics.get(0).genre_list.get(i).genre_name);
                            else
                                genrelist.append(",").append(detailComics.get(0).genre_list.get(i).genre_name);
                        }
                        genre.setText(genrelist.toString());
                        view.setText(detailComics.get(0).view);
                        desc.setText(detailComics.get(0).desc);
                        Picasso.get().load(detailComics.get(0).theme).into(theme);
                        Picasso.get().load(detailComics.get(0).thumb).into(thumb);
                        bottomSheetDialog = new MyBottomSheetFragement(getBaseContext(), detailComics.get(0).chapter_list);

                        dialog.dismiss();
                    }
                }));
    }


    @Override
    protected void onDestroy() {
        if(backGroundTask != null){
            backGroundTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void processFinish(String output) {
        chapter_endpoint = output;
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