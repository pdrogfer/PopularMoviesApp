package com.pgfmusic.popularmoviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    // Create some dummy photos list

    public String[] remotePhotos = {
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiguzbXcqtbFo-q8KigktHWg6mV8FIVISl-SkTSn8Ug4M073vJ",
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRjwRlJmBpdXi0Z6OfhddAKKqyq8159UwBCZh9bjju1T5nu1c8L",
            "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQJS9KVI97cU_2SzIl3fZrOUQB6NAmVMYNPV9LEycquvrFT3saxIQ",
            "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTEipUEyCrKbd6W-_Re3BRu-wCf1XzUXaC1i9_OUDtHHBK5csz-",
            "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRwlSmVoCXtfjEKEBFbsghGmXPernDWAblwc-Gxh56h78WVDt78",
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTbTlRyRb8FHjsuFrFDk8Bs_TAFJxpnYXeQbWtHqi8Ir_zGtlCL7Q"
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return remotePhotos.length;
    }

    @Override
    public Object getItem(int position) {
        return remotePhotos[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        // add placeholders and error image
        Picasso.with(mContext)
            .load(remotePhotos[position])
            .into(imageView);
        return imageView;
    }
}
