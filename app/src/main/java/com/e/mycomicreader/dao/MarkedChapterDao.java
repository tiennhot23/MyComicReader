package com.e.mycomicreader.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.e.mycomicreader.entity.MarkedChapter;

import java.util.List;

@Dao
public interface MarkedChapterDao {
    @Query("SELECT * FROM markedchapter")
    LiveData<List<MarkedChapter>> getAll();

//    @Query("SELECT * FROM markedchapter WHERE chapter_endpoint IN (:userIds)")
//    List<Comic> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM markedchapter WHERE endpoint LIKE :endpoint LIMIT 1")
    MarkedChapter findMarkedChapter(String endpoint);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(MarkedChapter... markedChapters);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MarkedChapter markedChapter);

    @Delete
    void delete(MarkedChapter markedChapter);

    @Update
    void update(MarkedChapter markedChapter);
}
