package com.e.mycomicreader.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.repository.FollowedComicRepository;

import java.util.List;

public class FollowedComicViewModel extends AndroidViewModel {
    private FollowedComicRepository FollowedComicRepository;

    private final LiveData<List<FollowedComic>> listFollowedComic;

    public FollowedComicViewModel (Application application) {
        super(application);
        FollowedComicRepository = new FollowedComicRepository(application);
        listFollowedComic = FollowedComicRepository.getAll();
    }

    public LiveData<List<FollowedComic>> getAll(){
        return listFollowedComic;
    }

    public FollowedComic findFollowedComic(String endpoint){
        return FollowedComicRepository.findFollowedComic(endpoint);
    }

    public void insertAll(FollowedComic... FollowedComics){
        FollowedComicRepository.insertAll(FollowedComics);
    }

    public void insert(FollowedComic FollowedComic){
        FollowedComicRepository.insert(FollowedComic);
    }

    public void delete(FollowedComic FollowedComic){

        FollowedComicRepository.delete(FollowedComic);
    }

    public void update(FollowedComic FollowedComic){
        FollowedComicRepository.update(FollowedComic);
    }
}
