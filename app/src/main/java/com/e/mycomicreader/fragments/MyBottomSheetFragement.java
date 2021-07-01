package com.e.mycomicreader.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.IRecylerClickListener;
import com.e.mycomicreader.R;
import com.e.mycomicreader.adapters.DetailComicAdapter;
import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.views.DownloadActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetFragement extends BottomSheetDialogFragment implements IRecylerClickListener{
    Context context;
    List<Chapter> chapter_list, download_chapters;
    DetailComic detailComic;

    private ImageView btn_download;

    public MyBottomSheetFragement() {
    }

    public MyBottomSheetFragement(Context context, List<Chapter> chapter_list, DetailComic detailComic) {
        this.context = context;
        this.chapter_list = chapter_list;
        this.detailComic = detailComic;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new DetailComicAdapter(context,chapter_list, detailComic, this));

        download_chapters = new ArrayList<>();

        btn_download = bottomSheetView.findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DownloadActivity.class);
                intent.putExtra("download_chapters", (Serializable) download_chapters);
                intent.putExtra("thumb", detailComic.thumb);
                intent.putExtra("theme", detailComic.theme);
                intent.putExtra("title", detailComic.title);
                String genre;
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<detailComic.genre_list.size(); i++){
                    stringBuilder.append(", ").append(detailComic.genre_list.get(i).genre_name);
                }
                genre = String.valueOf(stringBuilder).substring(2);
                intent.putExtra("genre", genre);
                intent.putExtra("desc", detailComic.desc);
                context.startActivity(intent);
            }
        });

        return bottomSheetDialog;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCheckBoxClick(int postion, String state) {
        if(state.equals("check")){
            download_chapters.add(chapter_list.get(postion));
        }else{
            download_chapters.remove(chapter_list.get(postion));
        }
    }
}
