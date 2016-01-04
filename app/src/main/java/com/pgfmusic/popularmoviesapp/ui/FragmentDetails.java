package com.pgfmusic.popularmoviesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class FragmentDetails extends Fragment implements View.OnClickListener{

    ImageView iv_poster;
    TextView tv_titleOriginal,
            tv_releaseDate,
            tv_userRating,
            tv_synopsis;
    FloatingActionButton isFavourite;
    int movieID;

    Context context = getActivity();

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
            Bundle movieDetails = getArguments();
            movieID = movieDetails.getInt(Utils.MOVIE_ID);
            iv_poster.setAdjustViewBounds(true);
            Picasso.with(getContext())
                    .load(movieDetails.getString(Utils.MOVIE_POSTER_PATH))
                    .into(iv_poster);
            tv_titleOriginal.setText(movieDetails.getString(Utils.MOVIE_ORIGINAL_TITLE));
            tv_releaseDate.setText("Release: " + movieDetails.getString(Utils.MOVIE_RELEASE_DATE));
            tv_userRating.setText("Rating: " + String.valueOf(movieDetails.getDouble(Utils.MOVIE_USER_RATING)));
            tv_synopsis.setText(movieDetails.getString(Utils.MOVIE_PLOT));

        } else {
            Intent intent = getActivity().getIntent();
            movieID = intent.getIntExtra(Utils.MOVIE_ID, 0);
            iv_poster.setAdjustViewBounds(true);
            Picasso.with(getContext())
                    .load(intent.getStringExtra(Utils.MOVIE_POSTER_PATH))
                    .into(iv_poster);
            tv_titleOriginal.setText(intent.getStringExtra(Utils.MOVIE_ORIGINAL_TITLE));
            tv_releaseDate.setText("Release: " + intent.getStringExtra(Utils.MOVIE_RELEASE_DATE));
            tv_userRating.setText("Rating: " + String.valueOf(intent.getDoubleExtra(Utils.MOVIE_USER_RATING, 0)));
            tv_synopsis.setText(intent.getStringExtra(Utils.MOVIE_PLOT));
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_favourite:

                // TODO: 04/01/2016 use database to store movies
                Snackbar.make(v, "Movie Favourite state changed from " + movieID + "to ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                break;
        }

    }
}
