package com.e.mycomicreader.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.IRecylerClickListener;
import com.e.mycomicreader.R;
import com.e.mycomicreader.adapters.DetailComicAdapter;
import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.DetailComic;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBottomSheetFragement extends BottomSheetDialogFragment {
    Context context;
    List<Chapter> chapter_list;
    DetailComic detailComic;
    IRecylerClickListener recylerClickListener;

    public MyBottomSheetFragement() {
    }

    public MyBottomSheetFragement(Context context, List<Chapter> chapter_list, DetailComic detailComic, IRecylerClickListener recylerClickListener) {
        this.context = context;
        this.chapter_list = chapter_list;
        this.detailComic = detailComic;
        this.recylerClickListener = recylerClickListener;
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
        recyclerView.setAdapter(new DetailComicAdapter(context,chapter_list, detailComic, recylerClickListener));
        return bottomSheetDialog;
    }
}
