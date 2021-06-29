package com.e.mycomicreader.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.fragments.MyBottomSheetFragement;
import com.e.mycomicreader.models.DetailComic;
import com.squareup.picasso.Picasso;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class DetailComicActivity extends AppCompatActivity {

    private String endpoint;
    private TextView title_comic, author, status, rating, updated_on, genre, view, desc, read, read_continue, chapter_list, btn_follow;
    private ImageView theme, thumb ,btn_go_back;
    private CompositeDisposable compositeDisposable;
    private Toast toast;

    IComicAPI iComicAPI;
    MyBottomSheetFragement bottomSheetDialog;
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

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        read_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    private void fetchDetailComic() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getDetailComic(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DetailComic>>() {
                    @Override
                    public void accept(List<DetailComic> detailComic) throws Exception {
                        title_comic.setText(detailComic.get(0).title);
                        author.setText(detailComic.get(0).author);
                        status.setText(detailComic.get(0).status);
                        rating.setText(detailComic.get(0).rating);
                        updated_on.setText(detailComic.get(0).updated_on);
                        StringBuilder genrelist = new StringBuilder();
                        for(int i=0; i<detailComic.get(0).genre_list.size(); i++){
                            if(i==0)
                                genrelist.append(detailComic.get(0).genre_list.get(i).genre_name);
                            else
                                genrelist.append(",").append(detailComic.get(0).genre_list.get(i).genre_name);
                        }
                        genre.setText(genrelist.toString());
                        view.setText(detailComic.get(0).view);
                        desc.setText(detailComic.get(0).desc);
                        Picasso.get().load(detailComic.get(0).theme).into(theme);
                        Picasso.get().load(detailComic.get(0).thumb).into(thumb);
                        bottomSheetDialog = new MyBottomSheetFragement(getBaseContext(), detailComic.get(0).chapter_list);

                        dialog.dismiss();
                    }
                }));
    }
}