package com.e.mycomicreader.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.models.Genre;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.lang.reflect.Field;
import java.util.List;

public class SearchFragment  extends Fragment {
    private CompositeDisposable compositeDisposable;

    View view;
    IComicAPI iComicAPI;
    RecyclerView recycler;
    Spinner spn_genre, spn_status;

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

        fetchGenres();

        fetchStatus();

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
                            genre_names[i] = genres.get(i).title;
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

    private void fetchStatus(){
        String[] status = new String[]{"Tất cả", "Đang tiến hành", "Hoàn thành"};
        ArrayAdapter statusArrayAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item, status);
        statusArrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item);
        spn_status.setAdapter(statusArrayAdapter);
    }
}
