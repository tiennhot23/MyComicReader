package com.e.mycomicreader.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.e.mycomicreader.entity.HistorySearch;
import com.e.mycomicreader.repository.HistorySearchRepository;

import java.util.List;

public class HistorySearchViewModel extends AndroidViewModel {
    private HistorySearchRepository HistorySearchRepository;

    private final LiveData<List<HistorySearch>> listHistorySearch;

    public HistorySearchViewModel (Application application) {
        super(application);
        HistorySearchRepository = new HistorySearchRepository(application);
        listHistorySearch = HistorySearchRepository.getAll();
    }

    public LiveData<List<HistorySearch>> getAll(){
        return listHistorySearch;
    }

    public HistorySearch findHistorySearch(String search_title){
        return HistorySearchRepository.findHistorySearch(search_title);
    }

    public void insertAll(HistorySearch... historySearchs){
        HistorySearchRepository.insertAll(historySearchs);
    }

    public void insert(HistorySearch historySearch){
        HistorySearchRepository.insert(historySearch);
    }

    public void delete(HistorySearch historySearch){

        HistorySearchRepository.delete(historySearch);
    }

    public void update(HistorySearch historySearch){
        HistorySearchRepository.update(historySearch);
    }
}
