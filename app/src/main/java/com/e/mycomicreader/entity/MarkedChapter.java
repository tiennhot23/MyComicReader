package com.e.mycomicreader.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MarkedChapter {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "endpoint")
    public String endpoint;
    @ColumnInfo(name = "chapter_endpoint")
    public String chapter_endpoint;

    public MarkedChapter(String endpoint, String chapter_endpoint) {
        this.endpoint = endpoint;
        this.chapter_endpoint = chapter_endpoint;
    }

    public MarkedChapter() {
    }
}
