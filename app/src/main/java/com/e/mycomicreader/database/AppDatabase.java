package com.e.mycomicreader.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.e.mycomicreader.dao.FollwedComicDao;
import com.e.mycomicreader.dao.HistorySearchDao;
import com.e.mycomicreader.dao.MarkedChapterDao;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.entity.HistorySearch;
import com.e.mycomicreader.entity.MarkedChapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FollowedComic.class, MarkedChapter.class, HistorySearch.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FollwedComicDao followedComicDao();
    public abstract MarkedChapterDao markedChapterDao();
    public abstract HistorySearchDao historySearchDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE historysearch(search_title TEXT)");
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "comic_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}