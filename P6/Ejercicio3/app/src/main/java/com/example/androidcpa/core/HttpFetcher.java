package com.example.androidcpa.core;

import android.os.AsyncTask;
import android.util.Log;

import com.example.androidcpa.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFetcher extends AsyncTask<URL, Void, Boolean> {
    public static final String LOG_TAG = "HttpFetcher";
    // public static final String CP_URL = "http://api.geonames.org/postalCodeSearchJSON?postalcode=32793&maxRows=100&username=dispositivos_moviles";

    public HttpFetcher(Observer activity)
    {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(URL... urls)
    {
        InputStream is = null;
        boolean toret = false;

        try {
            // Connection
            Log.d( LOG_TAG, " in doInBackground(): connecting" );
            HttpURLConnection conn = (HttpURLConnection) urls[ 0 ].openConnection();
            conn.setReadTimeout( 1000 /* milliseconds */ );
            conn.setConnectTimeout( 5000 /* milliseconds */ );
            conn.setRequestMethod( "POST" );
            conn.setDoInput( true );

            // Obtain the answer
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d( LOG_TAG, String.format( " in doInBackground(): server response is: %s(%d)",
                    conn.getResponseMessage(),
                    responseCode ) );

            if ( responseCode == 200 ) {
                this.responseParser = new ResponseParser( conn.getInputStream() );
                toret = true;
                Log.d( LOG_TAG, " in doInBackground(): finished" );
                Log.i( LOG_TAG, " in doInBackground(): fetching ok" );
            } else {
                Log.i( LOG_TAG, " in doInBackground(): fetching failed" );
            }
        }
        catch(IOException exc) {
            Log.e( LOG_TAG, " in doInBackground(), connecting: " + exc.getMessage() );
        } finally {
            Util.close( is, LOG_TAG, "doInBackground" );
        }

        return toret;
    }

    @Override
    public void onPostExecute(Boolean result)
    {
        if ( result ) {
            this.activity.setThInfo( this.responseParser.getTownHallList() );
        } else {

        }

        return;
    }

    private ResponseParser responseParser;
    private Observer activity;
}
