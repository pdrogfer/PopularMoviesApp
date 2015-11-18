package com.pgfmusic.popularmoviesapp;

public class Movie {

    int id;
    String title;
    String originalTitle;
    String poster;
    String plotSynopsis;
    double userRating;
    String releaseDate;

    public Movie(int id, String title, String originalTitle, String plotSynopsis, String poster, String releaseDate, double userRating) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }
}
