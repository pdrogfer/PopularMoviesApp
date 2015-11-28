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

    public static final String ORDER_KEY_PREFS = "order_movies_key";
    public static String SORT_ORDER = "popularity.desc";
    // TODO: 14/11/2015 remove api key from here so it doesnt go to github
    final String TMDB_API_KEY = "760291b7d6ef49594dc98e76ca41fb2d";

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
            SORT_ORDER = "popularity.desc";
        } else {
            SORT_ORDER = "vote_average.desc";
        }
        // TODO: 27/11/2015 store SORT_ORDER in Shared Preferences
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
                .appendQueryParameter("sort_by", SORT_ORDER)
                .appendQueryParameter("vote_count.gte", "100")
                .appendQueryParameter("api_key", TMDB_API_KEY);
        return builder.build().toString();
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
                    String posterPath = Utilities.TMDB_BASE_URL + Utilities.POSTER_SIZE + movie.getString(tag_posterPath);
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
