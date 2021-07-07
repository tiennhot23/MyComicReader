package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.models.FollowedComicViewModel;
import com.e.mycomicreader.views.MainActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowedFragment extends Fragment {

    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recycler;

    public FollowedComicViewModel followedComicViewModel;
    public static List<Comic> listFollowedComic = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_followed, container, false);

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        followedComicViewModel = new ViewModelProvider(this).get(FollowedComicViewModel.class);
        recycler = this.view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        if (MainActivity.isNetworkAvailable) {
            fetchFollowedComic(MainActivity.isFollowed);
            followedComicViewModel.getAll().observe(this, new Observer<List<FollowedComic>>() {
                @Override
                public void onChanged(List<FollowedComic> followedComics) {
                    listFollowedComic.clear();
                    for(int i=0; i<MainActivity.comics.size(); i++){
                        if(MainActivity.isFollowed.containsKey(MainActivity.comics.get(i).endpoint)){
                            listFollowedComic.add(MainActivity.comics.get(i));
                        }
                    }
                    recycler.getAdapter().notifyDataSetChanged();
                }
            });
        }



        return view;
    }

    private void fetchFollowedComic(HashMap<String, Boolean> isFollowed) {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
        dialog.show();
        System.out.println("FOLLOWED FRAGMENT");
            for(int i=0; i<MainActivity.comics.size(); i++){
                if(isFollowed.containsKey(MainActivity.comics.get(i).endpoint)){
                    listFollowedComic.add(MainActivity.comics.get(i));
                }
            }
        recycler.setAdapter(new ComicAdapter(view.getContext(), listFollowedComic, MainActivity.isFollowed));
        dialog.dismiss();
    }
}
