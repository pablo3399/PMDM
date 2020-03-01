package com.example.androidcpa.core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** ResponseParser interprets the answer obtained from the REST API */
public class ResponseParser {
    private static final String LOG_TAG = "ResponseParser";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String POSTAL_CODE = "postalCode";
    private static final String ADMIN_NAME2 = "adminName2";
    private static final String ADMIN_NAME1="adminName1";
    private static final String LONGITUD = "lng";
    private static final String PLACE_NAME="placeName";
    private static final String LATITUD = "lat";
    private static final String POSTAL_CODES = "postalCodes";

    /** Creates a new ResponseParser, given the InputStream from the connection */
    public ResponseParser(InputStream is)
    {
        this.parse( is );
    }

    /** @return a new DataDateTime object holding the info. */
    public static ArrayList<TownHall> getTownHallList()
    {
        return listaTh;
    }

    /** Parses the info from the given input stream. */
    private void parse(InputStream is)
    {
        String postalCode;
        double longitud;
        String countryCode;
        String adminName1;
        String adminName2;
        String placeName;
        double latitud;

        try {
            Log.d(LOG_TAG, " in doInBackground(): querying");
            JSONObject json = new JSONObject( readAllStream( is ) );
            Log.d(LOG_TAG, " in doInBackground(): content fetched: " + json.toString( 4 ));
            // Get basic info
            try{
                JSONArray jsonArray = json.getJSONArray("postalCodes");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    try {
                        adminName1 = object.getString( ADMIN_NAME1 ) ;
                    }catch(JSONException e){
                        adminName1=" ";
                    }

                    try {
                        adminName2 = object.getString( ADMIN_NAME2 ) ;
                    }catch(JSONException e){
                        adminName2=" ";
                    }

                    try {
                        longitud = object.getDouble( LONGITUD ) ;
                    }catch(JSONException e){
                        longitud=0.0;
                    }

                    try {
                        latitud = object.getDouble( LATITUD ) ;
                    }catch(JSONException e){
                        latitud=0.0;
                    }

                    try {
                        placeName = object.getString( PLACE_NAME ) ;
                    }catch(JSONException e){
                        placeName=" ";
                    }

                    try {
                         postalCode = object.getString( POSTAL_CODE ) ;
                    }catch(JSONException e){
                        postalCode=" ";
                    }

                    try {
                        countryCode = object.getString( COUNTRY_CODE ) ;
                    }catch(JSONException e){
                        countryCode=" ";
                    }

                    //rest of the strings..
                    this.th = new TownHall( countryCode, postalCode, longitud, latitud, placeName, adminName1, adminName2);
                    listaTh.add(this.th);

                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        } catch(JSONException exc) {
            Log.e( LOG_TAG, " in parse(): " + exc.getMessage() );
            this.th = TownHall.INVALID;
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

    private TownHall th;
    private static ArrayList<TownHall>listaTh=new ArrayList<>();
}

