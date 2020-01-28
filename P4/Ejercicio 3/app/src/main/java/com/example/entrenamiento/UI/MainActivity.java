package com.example.entrenamiento.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entrenamiento.Core.Entrenamiento;
import com.example.entrenamiento.Core.ListaDeEntrenamientos;
import com.example.entrenamiento.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class MainActivity extends AppCompatActivity {

    private int positionF;
    private ArrayAdapter<Entrenamiento> entrenamientoAdapter;
    public static final String EtqApp = "training";
    public static final String CfgFileName = "training.cfg";
    protected static final int CODIGO_EDICION_ITEM = 100;
    protected static final int CODIGO_ADICION_ITEM = 102;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //la siguiente linea es la declaracion de la clase que extiende de Applicaction, va a ser una lista de objetos
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplicationContext();
        //declaracion de los botones y el listview en el layout
        FloatingActionButton btAdd = (FloatingActionButton) this.findViewById(R.id.btAdd);
        FloatingActionButton btStats=(FloatingActionButton) this.findViewById(R.id.btStats);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvItems);

        lvItems.setLongClickable(true);

        this.entrenamientoAdapter = new ArrayAdapter<Entrenamiento>(
                this,
                android.R.layout.simple_selectable_list_item,
                list.getTrainingList()
        );

        //metodo que carga el xml que contiene la lista anterior almacenada por shared preferences
        loadState();

        lvItems.setAdapter(this.entrenamientoAdapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();

            }
        });

        btStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( MainActivity.this, MostrarStats.class );

                MainActivity.this.startActivity( subActividad );
            }
        });

    }

    //menu contextual
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi)
    {
        if ( view.getId() == R.id.lvItems)
        {
            this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );

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
             onEdit(index);
                toret = true;
                break;
            case R.id.context_delete:
                MainActivity.this.delete(index);
                toret = true;
                break;
        }

        return toret;
    }

    //menu de opciones
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
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplicationContext();
        boolean toret = false;
        String[] listItems = new String[list.getTrainingList().size()];
        for(int x = 0; x<list.getTrainingList().size(); x++){

            listItems[x]=list.getTrainingList().get(x).getNombre();

        }

        switch( menuItem.getItemId() ) {
            case R.id.añadir:
                MainActivity.this.onAdd();
                toret = true;
                break;
            case R.id.modificar:

                //dialogo de alerta que muestra por pantalla los posibles objetos para modificar
                AlertDialog.Builder getMod = new AlertDialog.Builder(MainActivity.this);
                getMod.setTitle("Cual quieres cambiar?");

                final int checkedItem = 0;
                getMod.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positionF=which;
                    }
                });

                getMod.setPositiveButton("go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      MainActivity.this.onEdit(positionF);
                      positionF=0;
                    }
                });

                getMod.setNegativeButton("cancel",null);

                getMod.create().show();
                toret = true;
                break;
            case R.id.eliminar:

                //dialogo de alerta para eliminar un elemento de la lista
                AlertDialog.Builder getDel = new AlertDialog.Builder(MainActivity.this);
                getDel.setTitle("Cual quieres eliminar?");

                for(int x = 0; x<list.getTrainingList().size(); x++){

                    listItems[x]=list.getTrainingList().get(x).getNombre();

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
                        list.getTrainingList().remove(positionF);

                        MainActivity.this.entrenamientoAdapter.notifyDataSetChanged();
                        MainActivity.this.updateStatus();
                    }
                });

                getDel.setNegativeButton("cancel",null);

                getDel.create().show();

                toret = true;
                break;

            case R.id.stats:
                //llamada a la actividad de mostrar estadisticas
                Intent subActividad = new Intent( MainActivity.this, MostrarStats.class );

                MainActivity.this.startActivity( subActividad );
                toret = true;
                break;
        }

        return toret;
    }

    //metodo que llama a la segunda aplicacion para añadir un entrenamiento
    private void onAdd() {

        Intent subActividad = new Intent( MainActivity.this, AddTraining.class );
        subActividad.putExtra( "pos", -1 );
        MainActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_ITEM );

    }

    //metodo que llama a la segunda aplicacion para modificar un entrenamiento
    private void onEdit(int pos) {

        Intent subActividad = new Intent( MainActivity.this, AddTraining.class );
        subActividad.putExtra( "pos", pos );
        MainActivity.this.startActivityForResult( subActividad, CODIGO_EDICION_ITEM );

    }

    //metodo para eliminar un entrenamiento de la lista
    private void delete(final int pos) {
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplicationContext();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Esta seguro de que quiere eliminar esta tarea?");
        b.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.getTrainingList().remove(pos);

                MainActivity.this.entrenamientoAdapter.notifyDataSetChanged();
                MainActivity.this.updateStatus();
            }});
        b.setNegativeButton("cancel", null);
        b.create().show();
    }

    //metodo que espera el codigo de otras activities y en caso de que se cumpla actualiza el arrayAdapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Recarga la lista y ejecuta el metodo updateStatus
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_ADICION_ITEM
                && resultCode == Activity.RESULT_OK) {

            this.entrenamientoAdapter.notifyDataSetChanged();
            this.updateStatus();
        }

        //Recarga la lista
        if (requestCode == CODIGO_EDICION_ITEM
                && resultCode == Activity.RESULT_OK) {
            this.entrenamientoAdapter.notifyDataSetChanged();
        }

        return;
    }

    //metodo que añade un numero al contador dependiendo de la cantidad de los entrenamiento agregados
    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.entrenamientoAdapter.getCount()));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //metodo apra guardar
        this.saveSate();
    }

    //metodo que almacena los entrenmientos en un xml para despues recuperarlos
    private void saveSate() {
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplicationContext();
        try (FileOutputStream f = this.openFileOutput( CfgFileName, Context.MODE_PRIVATE ) )
        {
            PrintStream cfg = new PrintStream( f );

            for(int x=0; x<list.getTrainingList().size(); x++){
                cfg.println( list.getTrainingList().get(x).getNombre() );
                cfg.println( list.getTrainingList().get(x).getTiempo() );
                cfg.println( list.getTrainingList().get(x).getDistancia() );
            }
            MainActivity.this.entrenamientoAdapter.notifyDataSetChanged();

            cfg.close();
        }
        catch(IOException exc) {
            Log.e( EtqApp, "Error saving state" );
        }


        System.out.println( "Saved state..." );
    }

    //metodo que carga los entrenamientos almacenados en el xml
    private void loadState() {
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplicationContext();
        try (FileInputStream f = this.openFileInput( CfgFileName  ) )
        {
            BufferedReader cfg = new BufferedReader( new InputStreamReader( f ) );

            String line = cfg.readLine();

            while( line != null ) {

                Entrenamiento entrenamiento = new Entrenamiento( line );
                entrenamiento.setTiempo( Float.parseFloat( cfg.readLine() ) );
                entrenamiento.setDistancia( Float.parseFloat( cfg.readLine() ) );
                list.getTrainingList().add( entrenamiento  );;
                MainActivity.this.entrenamientoAdapter.notifyDataSetChanged();
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

   /* private void detalles ( final int pos){

        final TextView textView = new TextView(this);
        String cadena = " En este entrenamiento se ha recorrido " + list.getTrainingList().get(pos).getDistancia() +
                " kilometros en " + list.getTrainingList().get(pos).getTiempo() + " Minutos " + ", los minutos por kilometro: " +
                list.getTrainingList().get(pos).minutosPorKilometro() + " y la velocidad media: "
                + list.getTrainingList().get(pos).kilometrosPorHora();
        textView.setText(cadena);

        builder.setTitle("Detalles del entrenamiento");
        builder.setView(textView);
        builder.setPositiveButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit(pos);
                    }
                });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();

    }*/

}
