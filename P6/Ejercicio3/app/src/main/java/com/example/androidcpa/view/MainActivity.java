package com.example.androidcpa.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcpa.R;
import com.example.androidcpa.core.HttpFetcher;
import com.example.androidcpa.core.Observer;
import com.example.androidcpa.core.ResponseParser;
import com.example.androidcpa.core.TownHall;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements Observer {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public String codigo;

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

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    EditText txt = (EditText)findViewById(R.id.edBuscar);
                    TextView tv = (TextView) findViewById(R.id.status);
                    InputFilter[] limiteNombre = new InputFilter[1];
                    limiteNombre[0] = new InputFilter.LengthFilter(10);
                    txt.setFilters(limiteNombre);

                    codigo = txt.getText().toString();


                    String cp_url = "http://api.geonames.org/postalCodeSearchJSON?postalcode="+codigo+"&maxRows=100&username=dispositivos_moviles";

                    new HttpFetcher(MainActivity.this ).execute( new URL( cp_url ) );
                } catch(MalformedURLException e)
                {
                    Log.e( "Timer.run", e.getMessage() );
                    Toast.makeText(getApplicationContext(), R.string.status_incorrect_url ,Toast.LENGTH_SHORT);
                }

                if(listaTh.isEmpty()){

                }else{
                    itemsAdapter.clear();
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi)
    {
        if ( view.getId() == R.id.lvItems)
        {
            this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );
            contextMenu.setHeaderTitle( R.string.menuC );
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        boolean toret = false;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int index = info.position;

        switch( menuItem.getItemId() ) {
            case R.id.alert:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Información");
                builder.setMessage(listaTh.get(index).toString2());
                builder.setNeutralButton("ok", null);
                builder.create().show();
                toret = true;
                break;
        }

        return toret;
    }

    public void setThInfo(ArrayList<TownHall> th)
    {
        listaTh = ResponseParser.getTownHallList();

        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);

        this.itemsAdapter = new ArrayAdapter<TownHall>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.listaTh);

        lvItems.setAdapter(this.itemsAdapter);
        if(listaTh.isEmpty()){
            TextView tv = (TextView) findViewById(R.id.status);
            tv.setText("No match");
        }
        lvItems.setLongClickable(true);
        this.registerForContextMenu(lvItems);

    }


    private boolean chkConnectivity()
    {
        Log.d( LOG_TAG, "checking connectivity" );
        ConnectivityManager connMgr =
                (ConnectivityManager)  this.getSystemService( Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean connected = ( networkInfo != null && networkInfo.isConnected() );


        return connected;
    }

    private ArrayList<TownHall> listaTh = new ArrayList<>();
    private int positionF;
    private ArrayAdapter<TownHall> itemsAdapter;
}
