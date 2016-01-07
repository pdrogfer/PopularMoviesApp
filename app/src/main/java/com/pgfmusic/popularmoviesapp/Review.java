package com.pgfmusic.popularmoviesapp;

/**
 * Created by Pedro on 07/01/2016.
 */
public class Review {

    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review(String author, String content) {

        this.author = author;
        this.content = content;
    }
}
