package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter2;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import java.io.File;

public class LibraryFragment  extends Fragment {

    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recycler;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_library, container, false);

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        recycler = this.view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        fetchLibrary();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchLibrary();
    }

    private void fetchLibrary() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
        dialog.show();
        File folder = new File(Environment.getExternalStoragePublicDirectory("/Download"), "/Download comic");
        if(!folder.exists()) folder.mkdirs();
        File listFile[] = folder.listFiles();
        recycler.setAdapter(new ComicAdapter2(view.getContext(), listFile));
        dialog.dismiss();
    }
}
