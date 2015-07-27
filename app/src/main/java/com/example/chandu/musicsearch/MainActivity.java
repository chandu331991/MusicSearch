package com.example.chandu.musicsearch;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String songsURL = "https://itunes.apple.com/search?term=tom";


    // JSON Node names
    private static final String TAG_RESULTS = "results";
    private static final String TAG_TRACKNAME = "trackName";
    private static final String TAG_ARTISTNAME = "artistName";
    private static final String TAG_COLLECTIONNAME = "collectionName";
    private static final String TAG_IMAGE = "artworkUrl30";

    // contacts JSONArray
    JSONArray results = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> resultsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String trackName = ((TextView) view.findViewById(R.id.trackName))
                        .getText().toString();
                String artistName = ((TextView) view.findViewById(R.id.artistName))
                        .getText().toString();
                String collectionName = ((TextView) view.findViewById(R.id.collectionName))
                        .getText().toString();

                Log.v("TEST", "TrackName " + trackName + "artistname " + artistName + "collectionName " + collectionName);

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleTrackActivity.class);
                in.putExtra(TAG_TRACKNAME, trackName);
                in.putExtra(TAG_ARTISTNAME, artistName);
                in.putExtra(TAG_COLLECTIONNAME, collectionName);
                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = String.valueOf(sh.makeServiceCall(songsURL, ServiceHandler.GET));

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    results = jsonObj.getJSONArray(TAG_RESULTS);

                    // looping through All Contacts
                  //  for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(1);

                        String trackname = c.getString(TAG_TRACKNAME);
                        String artistname = c.getString(TAG_ARTISTNAME);
                        String collectionname = c.getString(TAG_COLLECTIONNAME);


                        // tmp hashmap for single contact
                        HashMap<String, String> result = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        result.put(TAG_TRACKNAME, trackname);
                        result.put(TAG_ARTISTNAME, artistname);
                        result.put(TAG_COLLECTIONNAME, collectionname);


                        // adding contact to contact list
                        resultsList.add(result);
                  //  }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, resultsList,
                    R.layout.list_item, new String[] { TAG_TRACKNAME, TAG_ARTISTNAME,
                    TAG_COLLECTIONNAME,TAG_IMAGE}, new int[] { R.id.trackName,
                    R.id.artistName, R.id.collectionName,R.id.image });

            setListAdapter(adapter);
        }

    }

}
