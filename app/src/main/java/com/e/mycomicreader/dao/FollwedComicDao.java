package com.e.mycomicreader.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.e.mycomicreader.entity.FollowedComic;

import java.util.List;

@Dao
public interface FollwedComicDao {
    @Query("SELECT * FROM followedcomic")
    LiveData<List<FollowedComic>> getAll();

//    @Query("SELECT * FROM followedcomic WHERE endpoint IN (:comicIds)")
//    List<Comic> loadAllByIds(int[] comicIds);

    @Query("SELECT * FROM followedcomic WHERE endpoint LIKE :endpoint LIMIT 1")
    FollowedComic findFollowedComic(String endpoint);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(FollowedComic... followedComics);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FollowedComic followedComic);

    @Delete
    void delete(FollowedComic followedComic);

    @Update
    void update(FollowedComic followedComic);
}