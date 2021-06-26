package com.e.mycomicreader.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.e.mycomicreader.dao.FollwedComicDao;
import com.e.mycomicreader.database.AppDatabase;
import com.e.mycomicreader.entity.FollowedComic;

import java.util.List;

public class FollowedComicRepository {
    private FollwedComicDao followedComicDao;
    private LiveData<List<FollowedComic>> listFollowedComic;

    public FollowedComicRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        followedComicDao = db.followedComicDao();
        listFollowedComic = followedComicDao.getAll();
    }

    public LiveData<List<FollowedComic>> getAll(){
        return listFollowedComic;
    }

    public FollowedComic findFollowedComic(String endpoint){
        return followedComicDao.findFollowedComic(endpoint);
    }

    public void insertAll(FollowedComic... FollowedComics){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            followedComicDao.insertAll(FollowedComics);
        });
    }

    public void insert(FollowedComic FollowedComic){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            followedComicDao.insert(FollowedComic);
        });
    }

    public void delete(FollowedComic FollowedComic){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            followedComicDao.delete(FollowedComic);
        });
    }

    public void update(FollowedComic FollowedComic){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            followedComicDao.update(FollowedComic);
        });
    }
}
