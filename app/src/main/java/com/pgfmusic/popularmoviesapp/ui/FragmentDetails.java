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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pgfmusic.popularmoviesapp.DbHelper;
import com.pgfmusic.popularmoviesapp.Movie;
import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentDetails extends Fragment implements View.OnClickListener{

    ImageView iv_backdrop;
    TextView tv_titleOriginal,
            tv_releaseDate,
            tv_userRating,
            tv_synopsis;
    FloatingActionButton btnIsFavourite;
    Button btn_trailer;
    Button btn_reviews;
    ListView lv_reviews;
    Movie tempMovie;
    Bundle movieDetails;
    ArrayAdapter<String> adapterReviews;

    public FragmentDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        iv_backdrop = (ImageView) rootView.findViewById(R.id.ivDetailsBackdrop);
        tv_titleOriginal = (TextView) rootView.findViewById(R.id.tvDetailsTitle);
        tv_releaseDate = (TextView) rootView.findViewById(R.id.tvDetailsReleaseDate);
        tv_userRating = (TextView) rootView.findViewById(R.id.tvDetailsUserRating);
        tv_synopsis = (TextView) rootView.findViewById(R.id.tvDetailsSynopsis);
        btnIsFavourite = (FloatingActionButton) rootView.findViewById(R.id.btn_favourite);
        btn_trailer = (Button) rootView.findViewById(R.id.btn_trailer);
        btn_reviews = (Button) rootView.findViewById(R.id.btn_reviews);
        lv_reviews = (ListView) rootView.findViewById(R.id.lv_reviews);

        btnIsFavourite.setOnClickListener(this);
        btn_trailer.setOnClickListener(this);
        btn_reviews.setOnClickListener(this);

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
                movieDetails.getString(Utils.MOVIE_BACKDROP_PATH),
                movieDetails.getString(Utils.MOVIE_RELEASE_DATE),
                movieDetails.getDouble(Utils.MOVIE_USER_RATING),
                movieDetails.getInt(Utils.MOVIE_IS_FAVOURITE));

        iv_backdrop.setAdjustViewBounds(true);
        Picasso.with(getContext())
                .load(tempMovie.getBackdrop())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(iv_backdrop);
        tv_titleOriginal.setText(tempMovie.getOriginalTitle());
        tv_releaseDate.setText(getString(R.string.release_date) + " " + tempMovie.getReleaseDate());
        tv_userRating.setText(getString(R.string.user_rating)+  " " + String.valueOf(tempMovie.getUserRating()));
        tv_synopsis.setText(tempMovie.getPlotSynopsis());

        getTrailers(tempMovie.getId());

        String[] values = new String[]{};
        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        ArrayList<String> reviewsss = getMovieReviews(tempMovie.getId());
        adapterReviews = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        lv_reviews.setAdapter(adapterReviews);
        return rootView;
    }

    private ArrayList<String> getMovieReviews(int id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(id)) //movie ID, insert it as variable
                .appendPath("reviews")
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY);
        String trailerURL = builder.toString();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(trailerURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    Log.i(Utils.TAG, "Reviews OK: " + response);
                    String tag_results = "results";
                    String tag_author = "author";
                    String tag_content = "content";
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(tag_results);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonReview = jsonArray.getJSONObject(i);
                            String reviewAuthor = jsonReview.getString(tag_author).toUpperCase();
                            Log.i(Utils.TAG, "Author: " + reviewAuthor);
                            String reviewContent = jsonReview.getString(tag_content);
                            Log.i(Utils.TAG, "Content: " + reviewContent);
                            tempMovie.addReview(reviewAuthor + ": " + reviewContent);
                        }
                    } else {
                        // TODO: 07/01/2016 this does not work
                        Toast.makeText(getActivity(), "No reviews found for this movie", Toast.LENGTH_SHORT);
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.i(Utils.TAG, "Reviews ERROR: " + e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Utils.TAG, "Reviews Json ERROR: " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(Utils.TAG, "Reviews ERROR onFailure: " + error.toString());
            }
        });
        return tempMovie.getReviews();
    }

    private void getTrailers(int id)  {
        String key;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(id)) //movie ID, insert it as variable
                .appendPath("videos")
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY);
        String trailerURL = builder.toString();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(trailerURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    Log.i(Utils.TAG, "Trailer OK: " + new String(responseBody, "UTF-8"));
                    String tag_results = "results";
                    String tag_key = "key";
                    String tag_site = "site";
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(tag_results);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonTrailer = jsonArray.getJSONObject(i);
                        if (jsonTrailer.getString(tag_site).equals("YouTube")) {
                            tempMovie.setTrailerKey(jsonTrailer.getString(tag_key));
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.i(Utils.TAG, "Trailers ERROR: " + e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        Toast.makeText(getContext(), getString(R.string.removed_from_favourites), Toast.LENGTH_LONG)
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
                        newMovieValues.put(Utils.MOVIE_BACKDROP_PATH, tempMovie.getBackdrop());
                        newMovieValues.put(Utils.MOVIE_RELEASE_DATE, tempMovie.getReleaseDate());
                        newMovieValues.put(Utils.MOVIE_USER_RATING, tempMovie.getUserRating());
                        newMovieValues.put(Utils.MOVIE_IS_FAVOURITE, tempMovie.getIsFavourite());
                        Long i = db.insert(Utils.DB_MOVIES_TABLE_NAME, null, newMovieValues);
                        if (i > 0) {
                            Toast.makeText(getActivity(), getString(R.string.saved_to_favourites), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                db.close();
                break;

            case R.id.btn_trailer:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.YOUTUBE_BASE_URL + tempMovie.getTrailerKey()));
                startActivity(intent);
                break;
            case R.id.btn_reviews:
                adapterReviews.clear();
                adapterReviews.addAll(tempMovie.getReviews());
                adapterReviews.notifyDataSetChanged();
        }
    }

    public boolean deleteMovie(SQLiteDatabase db, Movie tempMovie) {
        boolean deleted = db.delete(Utils.DB_MOVIES_TABLE_NAME, Utils.MOVIE_ID + "=" +
                tempMovie.getId(), null) > 0;
        return deleted;
    }
}
