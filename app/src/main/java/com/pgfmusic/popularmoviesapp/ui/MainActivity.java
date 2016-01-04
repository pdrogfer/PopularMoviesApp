package com.pgfmusic.popularmoviesapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.pgfmusic.popularmoviesapp.R;
import com.pgfmusic.popularmoviesapp.Utils;
import com.pgfmusic.popularmoviesapp.ui.FragmentMain;

import org.json.JSONArray;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentMain())
                    .commit();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        if (prefs.contains(Utils.ORDER_KEY_PREFS)) {
            Utils.SORT_ORDER = prefs.getString(Utils.ORDER_KEY_PREFS, "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Utils.ORDER_KEY_PREFS)) {
            Utils.SORT_ORDER = sharedPreferences.getString(key, "");
        }
//        if (key.equals(Utils.FAVOURITES_KEY_PREFS)) {
//            // TODO: 04/01/2016 Dont know if this is necessary
//        }
    }
}
