package com.e.mycomicreader.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.e.mycomicreader.dao.MarkedChapterDao;
import com.e.mycomicreader.database.AppDatabase;
import com.e.mycomicreader.entity.MarkedChapter;

import java.util.List;

public class MarkedChapterRepository {
    private MarkedChapterDao markedChapterDao;
    private LiveData<List<MarkedChapter>> listMarkedChapter;

    public MarkedChapterRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        markedChapterDao = db.markedChapterDao();
        listMarkedChapter = markedChapterDao.getAll();
    }

    public LiveData<List<MarkedChapter>> getAll(){
        return listMarkedChapter;
    }

    public MarkedChapter findMarkedChapter(String endpoint){
        return markedChapterDao.findMarkedChapter(endpoint);
    }

    public void insertAll(MarkedChapter... markedChapters){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            markedChapterDao.insertAll(markedChapters);
        });
    }

    public void insert(MarkedChapter markedChapter){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            markedChapterDao.insert(markedChapter);
        });
    }

    public void delete(MarkedChapter markedChapter){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            markedChapterDao.delete(markedChapter);
        });
    }

    public void update(MarkedChapter markedChapter){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            markedChapterDao.update(markedChapter);
        });
    }
}
