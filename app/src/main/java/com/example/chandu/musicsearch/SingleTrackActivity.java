package com.example.chandu.musicsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by chandu on 7/24/2015.
 */
public class SingleTrackActivity extends Activity{


    // JSON node keys
    private static final String TAG_TRACKNAME = "trackName";
    private static final String TAG_ARTISTNAME = "artistName";
    private static final String TAG_COLLECTIONNAME = "collectionName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_track);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String trackname = in.getStringExtra(TAG_TRACKNAME );
        String artistname = in.getStringExtra(TAG_ARTISTNAME);
        String collectionname = in.getStringExtra(TAG_COLLECTIONNAME);


        // Displaying all values on the screen
        TextView lbltrackName = (TextView) findViewById(R.id.trackName_label);
        TextView lblartistName = (TextView) findViewById(R.id.artistName_label);
        TextView lblcollectionName = (TextView) findViewById(R.id.collectionName_label);


        lbltrackName.setText(trackname);
        lblartistName.setText(artistname);
        lblcollectionName.setText(collectionname);


    }



}
