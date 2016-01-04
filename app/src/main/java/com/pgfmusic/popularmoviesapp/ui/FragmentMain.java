package com.pgfmusic.popularmoviesapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pgfmusic.popularmoviesapp.FetchMoviesTask;
import com.pgfmusic.popularmoviesapp.ImageAdapter;
import com.pgfmusic.popularmoviesapp.Movie;
import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.pgfmusic.popularmoviesapp.ui.DetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class FragmentMain extends android.support.v4.app.Fragment implements
        AdapterView.OnItemClickListener {

    GridView gridViewMovies;
    ArrayList<Movie> movies;
    String strUrl;

    public FragmentMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_order_popularity:
                refreshGridView(1);
                return true;
            case R.id.menu_order_rating:
                refreshGridView(2);
            case R.id.menu_favourites:
                refreshGridView(3);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshGridView(int sortCriteria) {
        gridViewMovies.invalidateViews();
        if (sortCriteria == 1) {
            Utils.SORT_ORDER = Utils.ORDER_BY_POPULARITY;
        } else if (sortCriteria == 2) {
            Utils.SORT_ORDER = Utils.ORDER_BY_RATINGS;
        } else if (sortCriteria == 3) {
            /* TODO: 29/12/2015 retrieve favourites list from database/shared prefs and populate
            * 'movies' before calling 'gridViewMovies.setAdapter'
            * directly, no need to call FetchMoviesTask
            */


        }

        strUrl = buildURL();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        movies = null;
        try {
            movies = fetchMoviesTask.execute(strUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i(Utils.TAG, "No Movies Added");
        }
        gridViewMovies.setAdapter(new ImageAdapter(getActivity(), movies));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        strUrl = buildURL();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        movies = null;
        try {
            movies = fetchMoviesTask.execute(strUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i(Utils.TAG, "No Movies Added");
        }
        // fragment_main has phone and tablet versions
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridViewMovies = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridViewMovies.setOnItemClickListener(this);
        gridViewMovies.setAdapter(new ImageAdapter(getActivity(), movies));

        /* If the detail container is present, layout for tablet its been chosen, so we are in a
        * tablet. This decides how to handle clicks in gridViewMovies, in onItemClick */
        if (rootView.findViewById(R.id.movie_detail_container) != null) {
            Utils.TABLET_MODE = true;
        }
        return rootView;
    }

    private String buildURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", Utils.SORT_ORDER)
                .appendQueryParameter("vote_count.gte", "100")
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY);
        return builder.build().toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Movie tempMovie = movies.get(position);
        Log.i(Utils.TAG, "Clicked item num. " + position + ". Movie title: " +
                tempMovie.getTitle() + "User Rating: " + tempMovie.getUserRating());

        Bundle movieDetails = new Bundle();
        movieDetails.putInt(Utils.MOVIE_ID, tempMovie.getId());
        movieDetails.putString(Utils.MOVIE_TITLE, tempMovie.getTitle());
        movieDetails.putString(Utils.MOVIE_ORIGINAL_TITLE, tempMovie.getOriginalTitle());
        movieDetails.putString(Utils.MOVIE_PLOT, tempMovie.getPlotSynopsis());
        movieDetails.putString(Utils.MOVIE_POSTER_PATH, tempMovie.getPoster());
        movieDetails.putString(Utils.MOVIE_RELEASE_DATE, tempMovie.getReleaseDate());
        movieDetails.putDouble(Utils.MOVIE_USER_RATING, tempMovie.getUserRating());
        movieDetails.putInt(Utils.MOVIE_IS_FAVOURITE, tempMovie.getIsFavourite());
        if (Utils.TABLET_MODE) {
            FragmentDetails fragmentDetails = new FragmentDetails();
            fragmentDetails.setArguments(movieDetails);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragmentDetails)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("bundle", movieDetails);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save parcelled data
        outState.putParcelableArrayList("array_of_movies", movies);
    }

}
