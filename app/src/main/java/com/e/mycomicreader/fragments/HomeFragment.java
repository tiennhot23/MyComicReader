package com.e.mycomicreader.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.views.MainActivity;
import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recyclerLastUpdated, recyclerRecommend;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        recyclerLastUpdated = this.view.findViewById(R.id.recycler_last_updated);
        recyclerRecommend = this.view.findViewById(R.id.recycler_recommend);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerLastUpdated.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.view.getContext());
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        recyclerRecommend.setLayoutManager(linearLayoutManager1);

        if (MainActivity.isNetworkAvailable) {
            fetchLastUpdated();
            fetchRecommend();
        }
        MainActivity.followedComicViewModel.getAll().observe((LifecycleOwner) view.getContext(), new Observer<List<FollowedComic>>() {
            @Override
            public void onChanged(List<FollowedComic> followedComics) {
                recyclerLastUpdated.getAdapter().notifyDataSetChanged();
                recyclerRecommend.getAdapter().notifyDataSetChanged();
            }
        });


        return view;
    }

    private void fetchLastUpdated() {
        List<Comic> listLastUpdated = new ArrayList<>();
        if(MainActivity.comics.size() > 10)
            listLastUpdated.addAll(MainActivity.comics.subList(MainActivity.comics.size()-10, MainActivity.comics.size()-1));
        else
            listLastUpdated.addAll(MainActivity.comics);
        recyclerLastUpdated.setAdapter(new ComicAdapter(view.getContext(), listLastUpdated, MainActivity.isFolowed));
    }

    private void fetchRecommend(){
        List<Comic> listRecommend = new ArrayList<>();
        listRecommend = Common.sortRating(MainActivity.comics);
        if(MainActivity.comics.size() > 10)
            recyclerRecommend.setAdapter(new ComicAdapter(view.getContext(), listRecommend.subList(0,9), MainActivity.isFolowed));
        else
            recyclerRecommend.setAdapter(new ComicAdapter(view.getContext(), listRecommend, MainActivity.isFolowed));
    }
}
