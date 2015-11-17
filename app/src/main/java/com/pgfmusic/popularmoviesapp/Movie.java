package com.pgfmusic.popularmoviesapp;

/**
 * Created by USUARIO on 17/11/2015.
 */
public class Movie {

    String title;
    String id;
    String poster;

    public Movie(String id, String poster, String title) {
        this.id = id;
        this.poster = poster;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
