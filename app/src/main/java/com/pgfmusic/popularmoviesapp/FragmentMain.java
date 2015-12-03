package com.pgfmusic.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.concurrent.ExecutionException;

public class FragmentMain extends android.support.v4.app.Fragment implements
        AdapterView.OnItemClickListener {

    GridView gridView;
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_order_popularity) {
            refreshGridView(true);
            return true;
        } else if (id == R.id.menu_order_rating) {
            refreshGridView(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshGridView(Boolean sortCriteria) {
        gridView.invalidateViews();
        if (sortCriteria) {
            Utilities.SORT_ORDER = "popularity.desc";
        } else {
            Utilities.SORT_ORDER = "vote_average.desc";
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
            Log.i(Utilities.TAG, "No Movies Added");
        }
        gridView.setAdapter(new ImageAdapter(getActivity(), movies));
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
            Log.i(Utilities.TAG, "No Movies Added");
        }
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(new ImageAdapter(getActivity(), movies));
        return rootView;
    }

    private String buildURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", Utilities.SORT_ORDER)
                .appendQueryParameter("vote_count.gte", "100")
                .appendQueryParameter("api_key", Utilities.TMDB_API_KEY);
        return builder.build().toString();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Movie tempMovie = movies.get(position);
        Log.i(Utilities.TAG, "Clicked item num. " + position + ". Movie title: " +
                tempMovie.getTitle() + "User Rating: " + tempMovie.getUserRating());
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("id", tempMovie.getId());
        intent.putExtra("title", tempMovie.getTitle());
        intent.putExtra("original_title", tempMovie.getOriginalTitle());
        intent.putExtra("plot", tempMovie.getPlotSynopsis());
        intent.putExtra("poster_path", tempMovie.getPoster());
        intent.putExtra("release_date", tempMovie.getReleaseDate());
        intent.putExtra("user_rating", tempMovie.getUserRating());
        startActivity(intent);

    }

}
