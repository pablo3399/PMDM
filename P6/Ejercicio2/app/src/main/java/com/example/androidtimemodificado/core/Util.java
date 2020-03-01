package com.example.androidtimemodificado.core;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Util {
    /** Closes a stream. */
    public static void close(InputStream s, String logTag, String mth)
    {
        if ( s != null ) {
            try {
                s.close();
            } catch(IOException exc) {
                Log.e( logTag, mth + "(): closing stream: " + exc.getMessage() );
            }
        }

        return;
    }
}
