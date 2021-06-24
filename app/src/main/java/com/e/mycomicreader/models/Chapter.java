package com.e.mycomicreader.models;

import java.util.List;

public class Chapter {
    public String chapter_name;
    public String chapter_endpoint;
    public List<String> image_link;

    public Chapter() {
    }

    public Chapter(String chapter_name, String chapter_endpoint, List<String> image_link) {
        this.chapter_name = chapter_name;
        this.chapter_endpoint = chapter_endpoint;
        this.image_link = image_link;
    }
}
