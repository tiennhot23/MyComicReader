package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.views.MainActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class FollowedFragment extends Fragment {

    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recycler;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_followed, container, false);

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        recycler = this.view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        if (MainActivity.isNetworkAvailable) {
            fetchLastUpdated();
        }

        return view;
    }

    private void fetchLastUpdated() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getComics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler.setAdapter(new ComicAdapter(view.getContext(), comics));
                        dialog.dismiss();
                    }
                }));
    }
}
