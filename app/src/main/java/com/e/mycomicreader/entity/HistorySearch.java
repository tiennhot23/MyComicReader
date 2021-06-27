package com.e.mycomicreader.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistorySearch {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "search_title")
    public String search_title;

    public HistorySearch(@NonNull String search_title) {
        this.search_title = search_title;
    }

    public HistorySearch(){

    }
}
