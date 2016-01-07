package com.pgfmusic.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {

    int id;
    String title;
    String originalTitle;
    String plotSynopsis;
    String poster;
    String releaseDate; // TODO: Change format to Date
    double userRating;
    int isFavourite; // stores the favourite state of a movie, 0 = not, 1 = yes
    String trailerKey; // created by setter, in details view. Not in constructor
    ArrayList<String> reviews; // created by setter, in details view. Not in constructor

    public Movie(int id, String title, String originalTitle, String plotSynopsis, String poster,
                 String releaseDate, double userRating, int isFavourite) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.isFavourite = isFavourite;
        trailerKey = "";
        reviews = new ArrayList<>();
    }

    protected Movie(Parcel in) {
        // the order to read from parcel MUST be the same as writeToParcel
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        poster = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
        isFavourite = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {
        // empty constructor used to store temp values
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // the order of fields in writeToParcel must match the constructor Movie(Parcel in)
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsis);
        dest.writeString(poster);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
        dest.writeInt(isFavourite);
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

    public int getIsFavourite() {
        return isFavourite;
    }

    // change Favourite state of a movie, 0 = false, 1 = true
    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public void addReview(String review) {
        reviews.add(review);
    }
}
