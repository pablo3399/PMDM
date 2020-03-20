package com.example.navegadorweb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        final WebView wvView = (WebView) this.findViewById( R.id.wvView );
        final EditText busc = (EditText) this.findViewById(R.id.buscador);
        final ImageButton clear = (ImageButton) this.findViewById(R.id.clear);
        final ImageButton btn = (ImageButton) this.findViewById(R.id.btn);
        final ImageButton btnBack = (ImageButton) this.findViewById(R.id.btn_back);

        wvView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // Cargamos la web
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = busc.getText().toString();

                boolean result = validarUrl(url);

                if(result==true){

                    if(!url.contains("https://")) {
                        wvView.loadUrl("https://"+url);
                    }else if(!url.contains(".com")) {
                        wvView.loadUrl(url+".com");
                    }else{
                            wvView.loadUrl(url);
                        }

                }else{

                    wvView.loadUrl("https://www.google.com/search?q="+url);
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busc.setText("");
        }});

        wvView.getSettings().setBuiltInZoomControls(true);

        busc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    btn.performClick();
                }
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wvView.canGoBack()) {
                    wvView.goBack();
                }
            }
        });

    }

    private boolean validarUrl(String url) {

        if(url.contains(" ")){
            url.replaceAll(" ", "+");
            return false;
        }else {
            for(int x = 0; x<url.length(); x++){
                if(url.charAt(x)=='.' || url.charAt(x)=='/' || url.charAt(x)==':'){
                    return true;
                }

            }

        }
        return false;
    }

}
