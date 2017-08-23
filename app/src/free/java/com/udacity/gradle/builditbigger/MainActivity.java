package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.blackfrogweb.androidlibrary.DisplayJokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static String JOKE_KEY = "joke";
    private InterstitialAd mInterstitialAd;
    private Intent jokeIntent;
    private ProgressBar spinner;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIdlingResource();

        spinner = (ProgressBar)findViewById(R.id.progressBar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                startJokeActivity();
                Log.i("Ads", "onAdClosed");
            }
        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mIdlingResource != null) mIdlingResource.setIdleState(true);
    }

    private class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

        private MyApi myApiService = null;

        @Override
        protected String doInBackground(Void... params) {
            if(myApiService == null) {  // Only do this once

                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://builditbigger-177522.appspot.com/_ah/api/");

                myApiService = builder.build();
            }

            try {
                return myApiService.getJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Make spinner invisible
            spinner.setVisibility(View.GONE);
            jokeIntent = new Intent(getBaseContext(), DisplayJokeActivity.class);
            jokeIntent.putExtra(JOKE_KEY, result);
            //Set idling resource true
            if (mIdlingResource != null) mIdlingResource.setIdleState(true);

            //Show Ad if loaded
            if (mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
    }

    public void tellJoke(View view) {
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);
        //Make spinner visible
        spinner.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask().execute();
    }

    public void startJokeActivity(){
        //Start activity
        if(jokeIntent != null)
            startActivity(jokeIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
