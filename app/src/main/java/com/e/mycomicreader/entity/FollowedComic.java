package com.e.mycomicreader.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FollowedComic {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "endpoint")
    public String endpoint;

    public FollowedComic() {
    }

    public FollowedComic(@NonNull String endpoint) {
        this.endpoint = endpoint;
    }
}
