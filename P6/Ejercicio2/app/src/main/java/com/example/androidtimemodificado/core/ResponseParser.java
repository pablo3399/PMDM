package com.example.androidtimemodificado.core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** ResponseParser interprets the answer obtained from the REST API */
public class ResponseParser {
    private static final String LOG_TAG = "ResponseParser";
    private static final String COUNTRY_NAME_TAG = "countryName";
    private static final String TIMEZONE_TAG = "timezoneId";
    private static final String GMT_OFFSET_TAG = "gmtOffset";
    private static final String TIME_TAG = "time";
    private static final String SUNRISE = "sunrise";
    private static final String SUNSET = "sunset";
    private static final String LATITUD = "lat";


    /** Creates a new ResponseParser, given the InputStream from the connection */
    public ResponseParser(InputStream is)
    {
        this.parse( is );
    }

    /** @return a new DataDateTime object holding the info. */
    public DateTime getDateTime()
    {
        return this.data;
    }

    /** Parses the info from the given input stream. */
    private void parse(InputStream is)
    {
        String time = "";
        String timeInfo = "";
        String gmtInfo = "GMT";
        String sunriseInfo = "";
        String sunsetInfo ="";
        double latitudInfo = 0.0;

        try {
            Log.d(LOG_TAG, " in doInBackground(): querying");
            JSONObject json = new JSONObject( readAllStream( is ) );
            Log.d(LOG_TAG, " in doInBackground(): content fetched: " + json.toString( 4 ));

            // Get basic time info
            time = json.getString( TIME_TAG );
            timeInfo = json.getString( TIMEZONE_TAG ) + " (" + json.getString( COUNTRY_NAME_TAG ) + ")";

            // GMT info
            int gmtOffset = json.getInt( GMT_OFFSET_TAG );

            if ( gmtOffset != 0 ) {
                gmtInfo += " ";

                if ( gmtOffset > 0 ) {
                    gmtInfo += "+";
                }

                gmtInfo += Integer.toString( gmtOffset );
            }

            // Sunrise

             sunriseInfo = json.getString( SUNRISE );
             sunsetInfo  = json.getString( SUNSET );
             latitudInfo = json.getDouble(LATITUD);

            this.data = new DateTime( time, timeInfo, gmtInfo, sunriseInfo, sunsetInfo, latitudInfo );
        } catch(JSONException exc) {
            Log.e( LOG_TAG, " in parse(): " + exc.getMessage() );
            this.data = DateTime.INVALID;
        }
    }

    /** @return the whole stream contents in a single string. */
    private static String readAllStream(InputStream is)
    {
        BufferedReader reader = null;
        StringBuilder toret = new StringBuilder();
        String line;

        try {
            reader = new BufferedReader( new InputStreamReader( is ) );

            while( ( line = reader.readLine() ) != null ) {
                toret.append( line );
            }
        } catch (IOException e) {
            Log.e( LOG_TAG, " in getStringFromString(): error converting net input to string"  );
        } finally {
            Util.close( is, LOG_TAG, "readAllStream" );
        }

        return toret.toString();
    }

    private DateTime data;
}
