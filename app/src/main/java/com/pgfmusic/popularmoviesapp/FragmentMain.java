package com.pgfmusic.popularmoviesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class FragmentMain extends android.support.v4.app.Fragment {

    GridView gridView;

    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(new ImageAdapter(getActivity()));
        return rootView;
    }
}