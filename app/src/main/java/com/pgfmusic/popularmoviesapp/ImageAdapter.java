package com.pgfmusic.popularmoviesapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    public ArrayList<Movie> movies;

    public ImageAdapter(Context c, ArrayList<Movie> movies) {
        mContext = c;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        // TODO add placeholders and error poster

        Log.i(Utilities.TAG, movies.get(position).getPoster());

        Picasso.with(mContext)
            .load(movies.get(position).getPoster())
            .into(imageView);
        return imageView;
    }
}
