package com.e.mycomicreader.models;

import java.util.List;

public class DetailComic {
    public String title;
    public String status;
    public String rating;
    public String updated_on;
    public String desc;
    public String thumb;
    public String endpoint;
    public List<Genre> genre_list;
    public List<Chapter> chapter_list;

    public DetailComic() {
    }

    public DetailComic(String title, String status, String rating, String updated_on, String desc, String thumb, String endpoint, List<Genre> genre_list, List<Chapter> chapter_list) {
        this.title = title;
        this.status = status;
        this.rating = rating;
        this.updated_on = updated_on;
        this.desc = desc;
        this.thumb = thumb;
        this.endpoint = endpoint;
        this.genre_list = genre_list;
        this.chapter_list = chapter_list;
    }


}
