package com.blackfrogweb.androidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {

    public static String JOKE_KEY = "joke";
    private TextView mJokeTextView;
    private String passedJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        mJokeTextView = (TextView) findViewById(R.id.tv_joke);

        Intent intent = getIntent();
        //Check that the intent has the extra we need
        if(intent.hasExtra(JOKE_KEY)) {
            passedJoke = intent.getStringExtra(JOKE_KEY);
            //Check that the extra is not null
            if(passedJoke != null) {
                passedJoke = intent.getStringExtra(JOKE_KEY);
                mJokeTextView.setText(passedJoke);
            }
        }
    }
}
