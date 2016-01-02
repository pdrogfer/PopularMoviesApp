package com.pgfmusic.popularmoviesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.squareup.picasso.Picasso;

public class FragmentDetails extends Fragment {

    ImageView iv_poster;
    TextView tv_titleOriginal,
            tv_releaseDate,
            tv_userRating,
            tv_synopsis;

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
        Intent intent = getActivity().getIntent();
        if (intent != null) {

            int id = intent.getIntExtra("id", 0);
            iv_poster.setAdjustViewBounds(true);
            Picasso.with(getContext())
                    .load(intent.getStringExtra("poster_path"))
                    .into(iv_poster);
            tv_titleOriginal.setText(intent.getStringExtra("original_title"));
            tv_releaseDate.setText("Release: " + intent.getStringExtra("release_date"));
            tv_userRating.setText("Rating: " + String.valueOf(intent.getDoubleExtra("user_rating", 0)));
            tv_synopsis.setText(intent.getStringExtra("plot"));

        } else {
            Log.i(Utils.TAG, "intent null");
        }
        return rootView;
    }
}
