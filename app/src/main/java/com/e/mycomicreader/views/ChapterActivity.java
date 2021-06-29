package com.e.mycomicreader.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ChapterAdapter;
import com.e.mycomicreader.models.Chapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.Serializable;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    IComicAPI iComicAPI;
    private RecyclerView recycler;
    private int position;
    private List<Chapter> chapter_list;
    private String chapter_endpoint;
    private FloatingActionButton more, next, prev, beenhere;
    private Boolean isMoreClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        position = getIntent().getIntExtra("position", 0);
        chapter_list = (List<Chapter>) getIntent().getSerializableExtra("chapter_list");
        chapter_endpoint = chapter_list.get(position).chapter_endpoint;
        recycler = findViewById(R.id.recycler);
        more = findViewById(R.id.more);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        beenhere = findViewById(R.id.beenhere);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();

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
                if(position - 1 < 1){
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
                if(position + 1 > chapter_list.size()){
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

            }
        });
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
                        recycler.setAdapter(new ChapterAdapter(getApplication().getBaseContext(), chapter.get(0).chapter_image));
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
}