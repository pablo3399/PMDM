package com.example.appweb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    /**
     * Llamado cuando la actividad es creada.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );

        WebView wvView = (WebView) this.findViewById( R.id.wvView );


        this.configureWebView( wvView, "http://www.google.es", 18 );
    }

    private void configureWebView(WebView wvView, String url, int defaultFontSize)
    {
        WebSettings webSettings = wvView.getSettings();

        webSettings.setBuiltInZoomControls( true );
        webSettings.setDefaultFontSize( defaultFontSize );

        // Enable javascript and make android code available from it as the Android object.
        webSettings.setJavaScriptEnabled( true );
        wvView.addJavascriptInterface( new WebAppInterface( this ), "Android" );

        // URLs are handled by this WebView,instead of launching a browser
        wvView.setWebViewClient( new WebViewClient() );


        wvView.loadUrl( "file:///android_asset/calc.html" );
    }

}
