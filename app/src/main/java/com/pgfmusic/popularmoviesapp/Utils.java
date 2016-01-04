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

    public static final String FAVOURITES_KEY_PREFS = "favourites_list";
    public static final String ORDER_KEY_PREFS = "order_movies_key";
    public static final String ORDER_BY_POPULARITY = "popularity.desc";
    public static final String ORDER_BY_RATINGS = "vote_average.desc";
    // user can change sort order at run time
    public static String SORT_ORDER = ORDER_BY_POPULARITY;

    public static boolean TABLET_MODE = false;

    // Keys to identify movie values in bundle and intent transactions
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_ORIGINAL_TITLE = "original_title";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_USER_RATING = "user_rating";
    public static final String MOVIE_IS_FAVOURITE = "is_favourite";

}
