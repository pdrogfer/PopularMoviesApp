package com.pgfmusic.popularmoviesapp;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class Utils {

    public static final String TAG = "POPULAR_MOVIES";
    public static final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";

    public static final String APP_PREFERENCES = "app__prefs";

    public static final String POSTER_SIZE = "w342/";
    // Go to http://themoviedb.org/, get your own API Key and put it here
    public static String TMDB_API_KEY = "760291b7d6ef49594dc98e76ca41fb2d";

    public static final String ORDER_KEY_PREFS = "order_movies_key";
    public static final String ORDER_BY_POPULARITY = "popularity.desc";
    public static final String ORDER_BY_RATINGS = "vote_average.desc";
    public static String SORT_ORDER = ORDER_BY_POPULARITY;

}
