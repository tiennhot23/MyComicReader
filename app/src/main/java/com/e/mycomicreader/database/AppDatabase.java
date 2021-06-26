package com.e.mycomicreader.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.e.mycomicreader.dao.FollwedComicDao;
import com.e.mycomicreader.dao.MarkedChapterDao;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.entity.MarkedChapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FollowedComic.class, MarkedChapter.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FollwedComicDao followedComicDao();
    public abstract MarkedChapterDao markedChapterDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "comic_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}