package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter3;
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

import java.lang.reflect.Field;
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
    Spinner spn_genre, spn_status;
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

        spn_genre = this.view.findViewById(R.id.spn_genre);
        spn_status = this.view.findViewById(R.id.spn_status);
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
            fetchSearchComics("");
            fetchGenres();
            fetchStatus();
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isNetworkAvailable){
                    fetchSearchComics(editText.getText().toString());
                    spn_genre.setSelection(0);
                    spn_status.setSelection(0);
                    if(!editText.getText().toString().equals(""))
                        historySearchViewModel.insert(new HistorySearch(editText.getText().toString()));
                }
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isNetworkAvailable){
                    filter(editText.getText().toString(), spn_genre.getSelectedItem().toString());
                }
            }
        });

        return view;
    }

    private void fetchGenres() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Genre>>() {
                    @Override
                    public void accept(List<Genre> genres) throws Exception {
                        String[] genre_names = new String[genres.size()];
                        for (int i=0; i<genres.size(); i++) {
                            genre_names[i] = genres.get(i).genre_name;

                            genre_list.put(genres.get(i).genre_name, genres.get(i).genre_endpoint);
                        }
                        ArrayAdapter genreArrayAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item, genre_names);
                        genreArrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item);
                        spn_genre.setAdapter(genreArrayAdapter);
                        Field popup = Spinner.class.getDeclaredField("mPopup");
                        popup.setAccessible(true);
                        ListPopupWindow popupWindow = (ListPopupWindow) popup.get(spn_genre);
                        popupWindow.setHeight(500);

                        dialog.dismiss();
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
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics));
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
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics));
                            dialog.dismiss();
                        }
                    }));
        }

    }

    private void filter(String query, String genre){
        if(!query.equals("") && !genre.equals("Tất cả")){
            AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
            dialog.show();
            compositeDisposable.add(iComicAPI.filterComics(genre_list.get(genre), query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Comic>>() {
                        @Override
                        public void accept(List<Comic> comics) throws Exception {
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics));
                            dialog.dismiss();
                        }
                    }));
        }else if (query.equals("") && genre.equals("Tất cả")){
            recycler.setAdapter(new ComicAdapter3(view.getContext(), MainActivity.comics));
        }
        else if(query.equals("")){
            AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
            dialog.show();
            compositeDisposable.add(iComicAPI.filterComics(genre_list.get(genre))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Comic>>() {
                        @Override
                        public void accept(List<Comic> comics) throws Exception {
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics));
                            dialog.dismiss();
                        }
                    }));
        }else {
            AlertDialog dialog = new SpotsDialog.Builder().setContext(this.view.getContext()).setMessage("Loading...").build();
            dialog.show();
            compositeDisposable.add(iComicAPI.getSearchComic(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Comic>>() {
                        @Override
                        public void accept(List<Comic> comics) throws Exception {
                            recycler.setAdapter(new ComicAdapter3(view.getContext(), comics));
                            dialog.dismiss();
                        }
                    }));
        }
    }

    private void fetchStatus(){
        String[] status = new String[]{"Tất cả", "Đang tiến hành", "Hoàn thành"};
        ArrayAdapter statusArrayAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item, status);
        statusArrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item);
        spn_status.setAdapter(statusArrayAdapter);
    }
}
