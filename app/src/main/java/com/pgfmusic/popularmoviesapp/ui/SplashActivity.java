package com.pgfmusic.popularmoviesapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pgfmusic.popularmoviesapp.Utils;
import com.pgfmusic.popularmoviesapp.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by USUARIO on 30/11/2015.
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.TMDB_API_KEY == "") {

            SharedPreferences prefs = getSharedPreferences(Utils.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("api_key", getAPIKey("popularmoviesapp"));
        } else {
            goToMainActivity();
        }


    }

    public String getAPIKey(String appName) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://pgfmusic.com/popularmoviesapp/?app_name=popularmoviesapp", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String jsonAnswer = new String(response);
                Log.i("APP", jsonAnswer);
                try {
                    JSONObject jsonObject = new JSONObject(jsonAnswer);
                    Utils.TMDB_API_KEY = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.i(Utils.TAG, "Could not retrieve API Key");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFinish() {
                super.onFinish();
                goToMainActivity();
            }
        });
        return Utils.TMDB_API_KEY;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
