package com.example.androidtimemodificado.view;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidtimemodificado.R;
import com.example.androidtimemodificado.core.DateTime;
import com.example.androidtimemodificado.core.HttpFetcher;
import com.example.androidtimemodificado.core.Observer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements Observer {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );

        ImageButton btQuit = this.findViewById( R.id.btQuit );
        btQuit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        } );

        this.setDefaultValues();
    }

    @Override
    public void onPause() {
        super.onPause();

        if ( this.timer != null ) {
            this.timer.cancel();
            this.timer.purge();
        }

        return;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        this.setStatus( R.string.status_init );

        if ( this.chkConnectivity() ) {
            TimerTask taskFetchTime = new TimerTask() {
                @Override
                public void run() {
                    try {
                        new HttpFetcher(MainActivity.this ).execute( new URL( HttpFetcher.TIME_URL ) );
                    } catch(MalformedURLException e)
                    {
                        Log.e( "Timer.run", e.getMessage() );
                        MainActivity.this.setStatus( R.string.status_incorrect_url );
                    }
                }
            };

            // Program the task for every 20 seconds from now on.
            this.timer = new Timer();
            timer.schedule( taskFetchTime, 0, 20000 );
        }

        return;
    }

    public void setTimeInfo(DateTime data)
    {
        final TextView lblTime = this.findViewById( R.id.lblTime );
        final TextView lblGmtInfo = this.findViewById( R.id.lblGmtInfo );
        final TextView lblTimeZoneInfo = this.findViewById( R.id.lblTimeZoneInfo );
        final TextView lblSunrise = this.findViewById( R.id.lblSunrise );
        final TextView lblSunset = this.findViewById( R.id.lblSunset );
        final TextView lblLatitud = this.findViewById( R.id.lblLatitud );

        lblTime.setText( data.getDateTime() );
        lblTimeZoneInfo.setText( data.getTimeInfo() );
        lblGmtInfo.setText( data.getGmtInfo() );
        lblSunrise.setText( "Sunrise: " + data.getSunrise() );
        lblSunset.setText( "Sunset: " + data.getSunset());
        lblSunset.setText( "Latitude: " + String.valueOf(data.getLatitud()));
    }

    public void setStatus(int msgId)
    {
        final TextView lblStatus = this.findViewById( R.id.lblStatus );

        lblStatus.setText( msgId );
    }

    public void setDefaultValues()
    {
        final String notAvailable = this.getString( R.string.status_not_available );

        this.setTimeInfo( DateTime.INVALID );
        this.setStatus( R.string.status_error );
    }

    private boolean chkConnectivity()
    {
        Log.d( LOG_TAG, "checking connectivity" );
        ConnectivityManager connMgr =
                (ConnectivityManager)  this.getSystemService( Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean connected = ( networkInfo != null && networkInfo.isConnected() );

        if ( !connected ) {
            this.setStatus( R.string.status_not_connected );
        }

        return connected;
    }

    private Timer timer;
}
