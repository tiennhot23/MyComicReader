package com.e.mycomicreader.models;

import java.util.List;

public class Chapter {
    public String chapter_title;
    public String chapter_endpoint;
    public List<String> chapter_image;
    public String chapter_uploaded;

    public Chapter() {
    }

    public Chapter(String chapter_title, String chapter_endpoint, List<String> chapter_image) {
        this.chapter_title = chapter_title;
        this.chapter_endpoint = chapter_endpoint;
        this.chapter_image = chapter_image;
        this.chapter_uploaded = null;
    }

    public Chapter(String chapter_title, String chapter_endpoint, String chapter_uploaded) {
        this.chapter_title = chapter_title;
        this.chapter_endpoint = chapter_endpoint;
        this.chapter_image = null;
        this.chapter_uploaded = chapter_uploaded;
    }
    public Chapter(String chapter_title, String chapter_endpoint, List<String> chapter_image, String chapter_uploaded) {
        this.chapter_title = chapter_title;
        this.chapter_endpoint = chapter_endpoint;
        this.chapter_image = chapter_image;
        this.chapter_uploaded = chapter_uploaded;
    }

}
