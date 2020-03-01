package com.example.controlarwifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements DownloadCallback {


    private TextView mDataText;
    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataText = (TextView) findViewById(R.id.data_text);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");

         toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        //isntancia de Wifi manager
        wifiManager  = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        //Asigna el estado de wifi al toggleButton
        toggleButton.setChecked(wifiManager.isWifiEnabled());
        //Metodo para desactivar el wi-fi
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wifiManager.setWifiEnabled(isChecked);
                if(isChecked==true) {
                    mDataText.setText("Conectado con WIFI");
                }else{
                    mDataText.setText("Desconectado");
                }
            }
        }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
               getActiveNetworkInfo();
                return true;
            // Clear the text and cancel download.
            case R.id.clear_action:
                finishDownloading();
                mDataText.setText("");
                return true;
        }
        return false;
    }

    @Override
    public void getActiveNetworkInfo() {

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected==true){
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            if(isWiFi==true) {
                mDataText.setText("Conectado con " + activeNetwork.getTypeName());
                toggleButton.setChecked(true);
            }else{
                mDataText.setText("Conectado con " + activeNetwork.getTypeName());
            }
        }else{
            mDataText.setText("Desconectado");
            toggleButton.setChecked(false);
        }
    }

    @Override
    public void finishDownloading() {

    }

    private WifiManager wifiManager;
    private ToggleButton toggleButton;

}
