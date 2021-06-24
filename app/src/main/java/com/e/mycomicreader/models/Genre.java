package com.e.mycomicreader.models;

public class Genre {
    public String genre_name;
    public String genre_endpoint;

    public Genre() {
    }

    public Genre(String genre_name, String genre_endpoint) {
        this.genre_name = genre_name;
        this.genre_endpoint = genre_endpoint;
    }
}
