package com.pgfmusic.popularmoviesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by USUARIO on 14/11/2015.
 */
public class FragmentMain extends android.support.v4.app.Fragment {

    GridView gridView;

    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO: 14/11/2015 remove api key from here so it doesnt go to github
        final String TMDB_API_KEY = "760291b7d6ef49594dc98e76ca41fb2d";
        String strUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + TMDB_API_KEY;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        ArrayList<Movie> movies = null;
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
        // TODO: 17/11/2015 here pass the urls of the obtained photos to gridView
        gridView.setAdapter(new ImageAdapter(getActivity(), movies));
        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String answerJsonStr = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                answerJsonStr = buffer.toString();
                Log.i(Utilities.TAG, "JSON from TMDB: " + answerJsonStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(Utilities.TAG, "Error closing stream", e);
                    }
                }
            }
            return getMoviesDataFromJson(answerJsonStr);
        }

        private ArrayList<Movie> getMoviesDataFromJson(String JsonStr) {

            final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w500/";
            String tag_ID = "id";
            String tag_title = "title";
            String tag_posterPath = "poster_path";
            ArrayList<Movie> movies = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(JsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject movie = jsonArray.getJSONObject(i);
                    String movieID = movie.getString(tag_ID);
                    String movieTitle = movie.getString(tag_title);
                    String posterPath = movie.getString(tag_posterPath);

                    movies.add(new Movie(movieID, TMDB_BASE_URL + POSTER_SIZE + posterPath, movieTitle));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // TODO: 14/11/2015 I am getting the info from the movies
            // Log.i(Utilities.TAG, "Results: " + results);
            Log.i(Utilities.TAG, "Results: " + movies.toString());
            return movies;
        }
    }


}