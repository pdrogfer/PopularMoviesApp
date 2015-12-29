package com.pgfmusic.popularmoviesapp;

import android.os.AsyncTask;
import android.util.Log;

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
            Log.i(Utils.TAG, "JSON from TMDB: " + answerJsonStr);
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
                    Log.e(Utils.TAG, "Error closing stream", e);
                }
            }
        }
        return getMoviesDataFromJson(answerJsonStr);
    }

    private ArrayList<Movie> getMoviesDataFromJson(String JsonStr) {

        String tag_ID = "id";
        String tag_title = "title";
        String tag_originalTitle = "original_title";
        String tag_plot = "overview";
        String tag_posterPath = "poster_path";
        String tag_releaseDate = "release_date";
        String tag_userRating = "vote_average";
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(JsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject movie = jsonArray.getJSONObject(i);

                int movieID = Integer.parseInt(movie.getString(tag_ID));
                String movieTitle = movie.getString(tag_title);
                String originalTitle = movie.getString(tag_originalTitle);
                String plot = movie.getString(tag_plot);
                String posterPath = Utils.TMDB_BASE_URL + Utils.POSTER_SIZE + movie.getString(tag_posterPath);
                String releaseDate = movie.getString(tag_releaseDate);
                int userRating = movie.getInt(tag_userRating);

                movies.add(new Movie(movieID, movieTitle, originalTitle, plot, posterPath, releaseDate, userRating));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}