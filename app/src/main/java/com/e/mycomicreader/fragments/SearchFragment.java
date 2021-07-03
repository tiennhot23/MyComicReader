package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter3;
import com.e.mycomicreader.adapters.GenreAdapter;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.entity.HistorySearch;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.models.Genre;
import com.e.mycomicreader.models.HistorySearchViewModel;
import com.e.mycomicreader.views.MainActivity;
import com.nex3z.flowlayout.FlowLayout;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment  extends Fragment {
    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recycler;
    EditText editText;
    TextView genres;
    ImageView btn_search, btn_filter;
    FlowLayout flowLayout;
    Map<String, String> genre_list = new HashMap<>();

    List<Comic> list_search_comic = new ArrayList<>();

    public HistorySearchViewModel historySearchViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(
                R.layout.fragment_search, container, false);

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();
        recycler = this.view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this.view.getContext()));

        genres = this.view.findViewById(R.id.genres);
        editText = this.view.findViewById(R.id.edit_text);
        btn_search = this.view.findViewById(R.id.btn_search);
        btn_filter = this.view.findViewById(R.id.btn_filter);
        flowLayout = this.view.findViewById(R.id.flow_layout);
        historySearchViewModel = new ViewModelProvider(this).get(HistorySearchViewModel.class);
        historySearchViewModel.delete(new HistorySearch(""));
        historySearchViewModel.getAll().observe(this, new Observer<List<HistorySearch>>() {
            @Override
            public void onChanged(List<HistorySearch> historySearches) {
                if(historySearches.size() > 10){
                    historySearchViewModel.delete(historySearches.get(0));
                }
                flowLayout.removeAllViews();
                for(int i=0; i<historySearches.size(); i++){
                    TextView textView = new TextView(view.getContext());
                    ViewGroup.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(params);
                    textView.setId(i);
                    textView.setText(historySearches.get(i).search_title);
                    textView.setPadding(30,10,30,10);
                    textView.setBackgroundResource(R.drawable.flowlayout_item);
                    textView.setTextColor(getResources().getColor(R.color.black));

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.setText(textView.getText());
                        }
                    });

                    flowLayout.addView(textView);

                }
            }
        });


        if(MainActivity.isNetworkAvailable){
//            fetchSearchComics("");
            recycler.setAdapter(new ComicAdapter3(view.getContext(), MainActivity.comics, MainActivity.isFollowed));
            new Handler().post(() -> {
                fetchGenres();
            });
        }

        MainActivity.followedComicViewModel.getAll().observe((LifecycleOwner) view.getContext(), new Observer<List<FollowedComic>>() {
            @Override
            public void onChanged(List<FollowedComic> followedComics) {
                recycler.getAdapter().notifyDataSetChanged();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isNetworkAvailable){
                    fetchSearchComics(editText.getText().toString());
                    if(!editText.getText().toString().equals(""))
                        historySearchViewModel.insert(new HistorySearch(editText.getText().toString()));
                }
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        return view;
    }

    private void showOptionsDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Select Genres");

        View filter_layout = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_option, null);
        RecyclerView recyclerView = filter_layout.findViewById(R.id.recycler_option);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        GenreAdapter genreAdapter = new GenreAdapter(view.getContext(), Common.genres);
        recyclerView.setAdapter(genreAdapter);

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.setText("");
                genres.setText(genreAdapter.getFilterArray());
                filter(genreAdapter.getFilterArray());
            }
        });

        alertDialog.show();
    }

    private void fetchGenres() {
        compositeDisposable.add(iComicAPI.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Genre>>() {
                    @Override
                    public void accept(List<Genre> genres) throws Exception {
                        if(Common.genres != null) Common.genres.clear();
                        Common.genres.addAll(genres);
                    }
                }));
    }

    private void fetchSearchComics(String query) {
        if(query.equals("")){
            AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
            dialog.show();
            compositeDisposable.add(iComicAPI.getComics()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Comic>>() {
                        @Override
                        public void accept(List<Comic> comics) throws Exception {
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics, MainActivity.isFollowed));
                            dialog.dismiss();
                        }
                    }));
        }else{
            AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
            dialog.show();
            compositeDisposable.add(iComicAPI.getSearchComic(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Comic>>() {
                        @Override
                        public void accept(List<Comic> comics) throws Exception {
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics, MainActivity.isFollowed));
                            dialog.dismiss();
                        }
                    }));
        }

    }

    private void filter(String genre){
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.filterGenres(genre)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Comic>>() {
            @Override
            public void accept(List<Comic> comicList) throws Exception {
                recycler.setAdapter(new ComicAdapter3(view.getContext(), comicList, MainActivity.isFollowed));
                dialog.dismiss();
            }
        }));
    }
}
