package com.example.to_do.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.to_do.Core.AppList;
import com.example.to_do.Core.Item;
import com.example.to_do.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private int positionF;
    private ArrayAdapter<Item> itemsAdapter;

    public static final String EtqApp = "items";
    public static final String CfgFileName = "items.cfg";
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppList app = (AppList) this.getApplication();


        FloatingActionButton btAdd = (FloatingActionButton) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvItems);

        lvItems.setLongClickable(true);

        this.itemsAdapter = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_selectable_list_item,
                app.getItemList());

        lvItems.setAdapter(this.itemsAdapter);

        MainActivity.this.loadState();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent subActividad = new Intent( MainActivity.this, Add.class );
                subActividad.putExtra( "pos", -1 );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_ITEM );

            }
        });}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Recarga la lista y ejecuta el medodo updateStatus
        if ( requestCode == CODIGO_ADICION_ITEM
                && resultCode == Activity.RESULT_OK )
        {

            this.itemsAdapter.notifyDataSetChanged();
            this.updateStatus();
        }

        //Recarga la lista
        if ( requestCode == CODIGO_EDICION_ITEM
                && resultCode == Activity.RESULT_OK )
        {
            this.itemsAdapter.notifyDataSetChanged();
        }

        return;
    }

    @Override public void onResume() {
        super.onResume();
        final AppList app = (AppList) this.getApplication();

            int b = 0;
            boolean[] caduca2 = new boolean[app.getItemList().size()];
            final Calendar c = Calendar.getInstance();
            final int mesFin = c.get(Calendar.MONTH) + 1;
            final int diaFin = c.get(Calendar.DAY_OF_MONTH);
            final int anioFin = c.get(Calendar.YEAR);
            final ArrayList<Item> caducados = new ArrayList<Item>();
            final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
            final ArrayList<Item> borrados = new ArrayList<Item>();

            String fechaHoy = diaFin + "/" + mesFin + "/" + anioFin;

            for (int a = 0; a < app.getItemList().size(); a++) {

                String fecha = app.getItemList().get(a).getFecha();

                if (compararFechas(fecha, fechaHoy) == false) {
                    caducados.add(app.getItemList().get(a));
                    caduca2[b] = false;
                    b++;
                }
            }

            if (caducados.size() == 0) {
            } else {
                String[] listItems = new String[caducados.size()];
                for (int x = 0; x < caducados.size(); x++) {
                    listItems[x] = caducados.get(x).toString();
                }

                if (caducados.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Tareas caducadas");
                    builder.setMultiChoiceItems(listItems, caduca2, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean b) {
                            selectedItems.add(which);

                            for (int u = 0; u < selectedItems.size(); u++) {

                                borrados.add(caducados.get(selectedItems.get(u)));
                            }
                        }
                    });

                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Iterator<Item> iteratorItems = app.getItemList().iterator();
                                    //problem?
                                    while (iteratorItems.hasNext()) {
                                        if (borrados.contains(iteratorItems.next())) {
                                            iteratorItems.remove();
                                        }
                                    }

                                   /*for(int l = 0; l<borrados.size(); l++){
                                       String name = app.getItemList().get(l).getNombre();
                                       if( borrados.contains(name)) {
                                           app.getItemList().remove(l);
                                       }
                                       }*/

                                    itemsAdapter.notifyDataSetChanged();
                                    updateStatus();
                                }

                            }
                    );

                    builder.setNegativeButton("cancel", null);
                    builder.create().show();
                }

        }
    }

    public boolean compararFechas(String fecha, String fechaHoy){

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        Date fechaDate = null;
        Date fechaHoyFormateada=null;

        try{
            fechaDate = formato.parse(fecha);
            fechaHoyFormateada = formato.parse(fechaHoy);

        }catch ( ParseException e){ e.printStackTrace(); }

        if(fechaDate!=null){
        if(fechaDate.before(fechaHoyFormateada)|| fechaDate.equals(fechaHoyFormateada)){
            return false;
        }else{
            return true;
        }}else{return true;}
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
            case R.id.context_modify:
                Intent subActividad = new Intent( MainActivity.this, Add.class );
                subActividad.putExtra( "pos", index );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_EDICION_ITEM );
                toret = true;
                break;
            case R.id.context_delete:
                MainActivity.this.delete(index);
                toret = true;
                break;
        }

        return toret;
    }

    private void delete(final int pos) {
        final AppList app = (AppList) this.getApplication();

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Esta seguro de que quiere eliminar esta tarea?");
        b.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app.getItemList().remove(pos);
                MainActivity.this.itemsAdapter.notifyDataSetChanged();
                MainActivity.this.updateStatus();
            }});
        b.setNegativeButton("cancel", null);
        b.create().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.actions_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        final AppList app = (AppList) this.getApplication();
        boolean toret = false;
        String[] listItems = new String[app.getItemList().size()];
        for(int x = 0; x<app.getItemList().size(); x++){

            listItems[x]=app.getItemList().get(x).getNombre();

        }

        switch( menuItem.getItemId() ) {
            case R.id.añadir:
                Intent subActividad = new Intent( MainActivity.this, Add.class );
                subActividad.putExtra( "pos", -1 );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_ITEM );
                toret = true;
                break;
            case R.id.modificar:

                AlertDialog.Builder getMod = new AlertDialog.Builder(MainActivity.this);
                getMod.setTitle("Cual quieres cambiar?");


                final int checkedItem = 0;
                getMod.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positionF=which;
                    }
                });

                getMod.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent subActividad = new Intent( MainActivity.this, Add.class );
                        subActividad.putExtra( "pos", positionF );
                        MainActivity.this.startActivityForResult( subActividad, CODIGO_EDICION_ITEM );

                    }
                });

                getMod.setNegativeButton("cancel",null);

                getMod.create().show();
                toret = true;
                break;
            case R.id.eliminar:

                AlertDialog.Builder getDel = new AlertDialog.Builder(MainActivity.this);
                getDel.setTitle("Cual quieres eliminar?");

                for(int x = 0; x<app.getItemList().size(); x++){

                    listItems[x]=app.getItemList().get(x).getNombre();

                }

                final int checkedIt = 0;
                getDel.setSingleChoiceItems(listItems, checkedIt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positionF=which;
                    }
                });

                getDel.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        app.getItemList().remove(positionF);
                        MainActivity.this.itemsAdapter.notifyDataSetChanged();
                        MainActivity.this.updateStatus();
                    }
                });

                getDel.setNegativeButton("cancel",null);

                getDel.create().show();

                toret = true;
                break;
        }

        return toret;
    }


    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.itemsAdapter.getCount()));
    }



    @Override
    public void onStop()
    {
        super.onStop();
        this.saveSate();
    }

    private void saveSate() {
        final AppList app = (AppList) this.getApplication();

        try (FileOutputStream f = this.openFileOutput( CfgFileName, Context.MODE_PRIVATE ) )
        {
            PrintStream cfg = new PrintStream( f );

            for(int x=0; x<app.getItemList().size(); x++) {
                cfg.println( app.getItemList().get(x).getNombre() );
                System.out.println(app.getItemList().get(x).getNombre());
                cfg.println( app.getItemList().get(x).getFecha() );
                System.out.println(app.getItemList().get(x).getFecha());
                System.out.println(app.getItemList().get(x));
            }

            MainActivity.this.itemsAdapter.notifyDataSetChanged();

            cfg.close();
        }
        catch(IOException exc) {
            Log.e( EtqApp, "Error saving state" );
        }



        System.out.println( "Saved state..." );
    }

    private void loadState() {
        final AppList app = (AppList) this.getApplication();

        try (FileInputStream f = this.openFileInput( CfgFileName  ) )
        {
            BufferedReader cfg = new BufferedReader( new InputStreamReader( f ) );

            String line = cfg.readLine();

            while( line != null ) {

                Item item = new Item( line );
                item.setFecha( cfg.readLine() );
                app.getItemList().add(item);
                MainActivity.this.itemsAdapter.notifyDataSetChanged();
                MainActivity.this.updateStatus();
                line = cfg.readLine();
            }

            cfg.close();

        }
        catch (IOException exc)
        {
            Log.e( EtqApp, "Error loading state" );
        }

        System.out.println( "Loaded state..." );
    }


}