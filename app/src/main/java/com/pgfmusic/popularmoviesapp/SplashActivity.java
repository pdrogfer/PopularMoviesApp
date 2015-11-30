package com.pgfmusic.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

        if (Utilities.TMDB_API_KEY == "") {

            getAPIKey("popularmoviesapp");

        }


    }

    public void getAPIKey(String appName) {
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
                    Utilities.TMDB_API_KEY = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.i(Utilities.TAG, "Could not retrieve API Key");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFinish() {
                super.onFinish();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
