package com.pgfmusic.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by USUARIO on 18/11/2015.
 */
public class FragmentDetails extends Fragment {

    public FragmentDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            // TODO: 18/11/2015 recover the parameters passed, that is the details of the movie to display

            int id = intent.getIntExtra("id", 0);
            String original_title = intent.getStringExtra("original_title");
            String plot = intent.getStringExtra("plot");
            String poster_path = intent.getStringExtra("poster_path");
            int rating = intent.getIntExtra("user_rating", 0);
            String release_date = intent.getStringExtra("release_date");

            Toast.makeText(getActivity(), id + " " + original_title + " " +
                            poster_path + " " + plot + " " + rating + " " + release_date,
                    Toast.LENGTH_SHORT).show();
            Log.i(Utilities.TAG, id + " " + original_title + " " + poster_path + " " +
                    plot + " " + rating + " " + release_date);
        } else {
            Log.i(Utilities.TAG, "intent null");
        }

        return rootView;
    }
}
