package com.pgfmusic.popularmoviesapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pgfmusic.popularmoviesapp.DbHelper;
import com.pgfmusic.popularmoviesapp.Movie;
import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class FragmentDetails extends Fragment implements View.OnClickListener{

    ImageView iv_poster;
    TextView tv_titleOriginal,
            tv_releaseDate,
            tv_userRating,
            tv_synopsis;
    FloatingActionButton isFavourite;
    Movie tempMovie;
    Bundle movieDetails;

    public FragmentDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        iv_poster = (ImageView) rootView.findViewById(R.id.ivDetailsPoster);
        tv_titleOriginal = (TextView) rootView.findViewById(R.id.tvDetailsTitle);
        tv_releaseDate = (TextView) rootView.findViewById(R.id.tvDetailsReleaseDate);
        tv_userRating = (TextView) rootView.findViewById(R.id.tvDetailsUserRating);
        tv_synopsis = (TextView) rootView.findViewById(R.id.tvDetailsSynopsis);
        isFavourite = (FloatingActionButton) rootView.findViewById(R.id.btn_favourite);
        isFavourite.setOnClickListener(this);

        if (Utils.TABLET_MODE) {
            movieDetails = getArguments();
        } else {
            Intent in = getActivity().getIntent();
            movieDetails = in.getBundleExtra("bundle");
        }
        tempMovie = new Movie(movieDetails.getInt(Utils.MOVIE_ID),
                movieDetails.getString(Utils.MOVIE_TITLE),
                movieDetails.getString(Utils.MOVIE_ORIGINAL_TITLE),
                movieDetails.getString(Utils.MOVIE_PLOT),
                movieDetails.getString(Utils.MOVIE_POSTER_PATH),
                movieDetails.getString(Utils.MOVIE_RELEASE_DATE),
                movieDetails.getDouble(Utils.MOVIE_USER_RATING),
                movieDetails.getInt(Utils.MOVIE_IS_FAVOURITE));

        iv_poster.setAdjustViewBounds(true);
        Picasso.with(getContext())
                .load(tempMovie.getPoster())
                .into(iv_poster);
        tv_titleOriginal.setText(tempMovie.getOriginalTitle());
        tv_releaseDate.setText("Release: " + tempMovie.getReleaseDate());
        tv_userRating.setText("Rating: " + String.valueOf(tempMovie.getUserRating()));
        tv_synopsis.setText(tempMovie.getPlotSynopsis());
        // TODO: 04/01/16 set btnFavourite status

        getTrailers();
        getReviews();


        return rootView;
    }

    private void getReviews() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("286217") //movie ID, insert it as variable
                .appendPath("reviews")
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY);
        String trailerURL = builder.toString();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(trailerURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.i(Utils.TAG, "Reviews OK: " + new String(responseBody, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.i(Utils.TAG, "Reviewss ERROR: " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(Utils.TAG, "Reviews ERROR onFailure: " + error.toString());
            }
        });
    }

    private void getTrailers()  {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("286217") //movie ID, insert it as variable
                .appendPath("videos")
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY);
        String trailerURL = builder.toString();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(trailerURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.i(Utils.TAG, "Trailer OK: " + new String(responseBody, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.i(Utils.TAG, "Trailers ERROR: " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i(Utils.TAG, "Trailers ERROR onFailure: " + error.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_favourite:

                DbHelper dbHelper = new DbHelper(getContext(), Utils.DB_MOVIES, null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (tempMovie.getIsFavourite() == 1) {
                    // movie is in Favourites list. Remove it from there
                    // and call tempMovie.setIsFavourite(0);
                    boolean result = deleteMovie(db, tempMovie);
                    if (result) {
                        Toast.makeText(getContext(), "Movie removed from Favourites list", Toast.LENGTH_LONG)
                                .show();
                        tempMovie.setIsFavourite(0);
                    }
                } else {
                    // movie is not in Favourites. Call tempMovie.setIsFavourite(1) and add it to
                    // database
                    if (db != null) {
                        tempMovie.setIsFavourite(1);
                        ContentValues newMovieValues = new ContentValues();
                        newMovieValues.put(Utils.MOVIE_ID, tempMovie.getId());
                        newMovieValues.put(Utils.MOVIE_TITLE, tempMovie.getTitle());
                        newMovieValues.put(Utils.MOVIE_ORIGINAL_TITLE, tempMovie.getOriginalTitle());
                        newMovieValues.put(Utils.MOVIE_PLOT, tempMovie.getPlotSynopsis());
                        newMovieValues.put(Utils.MOVIE_POSTER_PATH, tempMovie.getPoster());
                        newMovieValues.put(Utils.MOVIE_RELEASE_DATE, tempMovie.getReleaseDate());
                        newMovieValues.put(Utils.MOVIE_USER_RATING, tempMovie.getUserRating());
                        newMovieValues.put(Utils.MOVIE_IS_FAVOURITE, tempMovie.getIsFavourite());
                        Long i = db.insert(Utils.DB_MOVIES_TABLE_NAME, null, newMovieValues);
                        if (i > 0) {
                            Toast.makeText(getActivity(), "Movie saved in Favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                db.close();
                break;
        }
    }

    public boolean deleteMovie(SQLiteDatabase db, Movie tempMovie) {
        boolean deleted = db.delete(Utils.DB_MOVIES_TABLE_NAME, Utils.MOVIE_ID + "=" +
                tempMovie.getId(), null) > 0;
        return deleted;
    }
}
