package com.pgfmusic.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pgfmusic.popularmoviesapp.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_details, new FragmentDetails())
                    .commit();
        }
    }


}
