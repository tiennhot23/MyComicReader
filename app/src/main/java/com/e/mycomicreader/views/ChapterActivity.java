package com.e.mycomicreader.views;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ChapterAdapter;
import com.e.mycomicreader.models.Chapter;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    IComicAPI iComicAPI;
    private RecyclerView recycler;
    private String chapter_endpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        chapter_endpoint = getIntent().getStringExtra("chapter_endpoint");
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();

        fetchChapter();

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
}