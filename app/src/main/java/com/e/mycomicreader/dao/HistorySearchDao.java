package com.e.mycomicreader.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.e.mycomicreader.entity.HistorySearch;

import java.util.List;

@Dao
public interface HistorySearchDao {
    @Query("SELECT * FROM historysearch")
    LiveData<List<HistorySearch>> getAll();

//    @Query("SELECT * FROM HistorySearch WHERE chapter_endpoint IN (:userIds)")
//    List<Comic> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM historysearch WHERE search_title LIKE :search_title LIMIT 1")
    HistorySearch findHistorySearch(String search_title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(HistorySearch... historysearchs);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HistorySearch historysearch);

    @Delete
    void delete(HistorySearch historysearch);

    @Update
    void update(HistorySearch historysearch);
}
