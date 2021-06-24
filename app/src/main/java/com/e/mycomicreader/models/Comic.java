package com.e.mycomicreader.models;

import java.util.List;

public class Comic {
    public String title;
    public String status;
    public String rating;
    public String updated_on;
    public String thumb;
    public String endpoint;
    public List<Genre> genre_list;
    public String chapter;

    public Comic() {
    }

    public Comic(String title, String status, String rating, String updated_on, String thumb, String endpoint, List<Genre> genre_list, String chapter) {
        this.title = title;
        this.status = status;
        this.rating = rating;
        this.updated_on = updated_on;
        this.thumb = thumb;
        this.endpoint = endpoint;
        this.genre_list = genre_list;
        this.chapter = chapter;
    }
}
