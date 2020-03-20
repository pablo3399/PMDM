package com.example.listaweb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.listaweb.DBManager.TABLA_LCOMPRA;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_main);
        WebView wvView = (WebView) this.findViewById( R.id.wvView );

        this.configureWebView( wvView, "http://www.google.es", 22 );
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

        wvView.loadUrl( "file:///android_asset/lista.html" );
    }

    private DBManager gestorDB;
}
