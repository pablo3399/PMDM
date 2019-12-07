package com.example.ejercicio3.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejercicio3.R;
import com.example.ejercicio3.core.Entrenamiento;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String EtqApp = "training";
    public static final String CfgFileName = "training.cfg";
    private int positionF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();
        this.arrayDYT = new ArrayList<Entrenamiento>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);
        this.registerForContextMenu(lvItems);

        lvItems.setLongClickable(true);
        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter(this.itemsAdapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();

            }
        });


    }

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
            case R.id.context_details:
                MainActivity.this.detalles(index);
                toret = true;
                break;
            case R.id.context_modify:
                MainActivity.this.edit(index);
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
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Esta seguro de que quiere eliminar esta tarea?");
        b.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.items.remove(pos);
                MainActivity.this.arrayDYT.remove(pos);
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
        boolean toret = false;
        String[] listItems = new String[items.size()];
        for(int x = 0; x<items.size(); x++){

            listItems[x]=items.get(x);

        }

        switch( menuItem.getItemId() ) {
            case R.id.añadir:
                MainActivity.this.onAdd();
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
                        MainActivity.this.edit(positionF);
                    }
                });

                getMod.setNegativeButton("cancel",null);

                getMod.create().show();
                toret = true;
                break;
            case R.id.eliminar:

                AlertDialog.Builder getDel = new AlertDialog.Builder(MainActivity.this);
                getDel.setTitle("Cual quieres eliminar?");

                for(int x = 0; x<items.size(); x++){

                    listItems[x]=items.get(x);

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
                        MainActivity.this.items.remove(positionF);
                        MainActivity.this.arrayDYT.remove(positionF);
                        MainActivity.this.itemsAdapter.notifyDataSetChanged();
                        MainActivity.this.updateStatus();
                    }
                });

                getDel.setNegativeButton("cancel",null);

                getDel.create().show();

                toret = true;
                break;

            case R.id.stats:
                MainActivity.this.mostrarStats();
                toret = true;
                break;
        }

        return toret;
    }


    private void onAdd() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_training_values,null);
        builder.setView(customLayout);

        builder.setTitle("Entrenamiento");
        builder.setMessage("añade los detalles del entrenamiento:");

        final EditText edText1 = (EditText) customLayout.findViewById(R.id.name);
        final EditText edText2 = (EditText) customLayout.findViewById(R.id.time);
        final EditText edText3 = (EditText) customLayout.findViewById(R.id.distance);

        builder.setPositiveButton("+", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (edText1.getText().toString().trim().isEmpty() || edText2.getText().toString().trim().isEmpty()
                            || edText3.getText().toString().trim().isEmpty()){
                        vacioAdd();
                }else {

                    String nombre = (edText1.getText().toString());
                    float tiempo = Float.valueOf(edText2.getText().toString());
                    float distancia= Float.valueOf(edText3.getText().toString());

                    Entrenamiento entrenamiento = new Entrenamiento(nombre, tiempo, distancia);
                    arrayDYT.add(entrenamiento);

                    MainActivity.this.itemsAdapter.add(edText1.getText().toString());
                    MainActivity.this.updateStatus();

                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.itemsAdapter.getCount()));
    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private ArrayList<Entrenamiento> arrayDYT;

    private void detalles(final int pos) {

        final TextView textView = new TextView( this);
        String cadena = " En este entrenamiento se ha recorrido " + MainActivity.this.arrayDYT.get(pos).getDistancia()+
                " kilometros en " + MainActivity.this.arrayDYT.get(pos).getTiempo() + " Minutos " + ", los minutos por kilometro: "+
                MainActivity.this.arrayDYT.get(pos).minutosPorKilometro() + " y la velocidad media: "
                + MainActivity.this.arrayDYT.get(pos).kilometrosPorHora();
        textView.setText(cadena);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    }

    private void edit(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_training_edit,null);
        builder.setView(customLayout);


        builder.setTitle("Modificar entrenamiento");

        final EditText edText1 = (EditText) customLayout.findViewById(R.id.name);
        final EditText edText2 = (EditText) customLayout.findViewById(R.id.time);
        final EditText edText3 = (EditText) customLayout.findViewById(R.id.distance);

        edText1.setText(MainActivity.this.arrayDYT.get(pos).getNombre());
        String tiempo = String.valueOf(MainActivity.this.arrayDYT.get(pos).getTiempo());
        edText2.setText(tiempo);
        String distancia = String.valueOf(MainActivity.this.arrayDYT.get(pos).getDistancia());
        edText3.setText(distancia);

        builder.setPositiveButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                         if (edText1.getText().toString().trim().isEmpty() &&  edText2.getText().toString().trim().isEmpty()){
                            edText2.setText("0");
                            vacioEdit(pos);
                        }  if (edText1.getText().toString().trim().isEmpty() &&  edText3.getText().toString().trim().isEmpty()){
                            edText3.setText("0");
                            vacioEdit(pos);
                        }
                         if(edText1.toString().trim().isEmpty() && edText2.getText().toString().trim().isEmpty()
                                 && edText3.getText().toString().trim().isEmpty()){
                             edText2.setText("0");
                             edText3.setText("0");
                             vacioEdit(pos);
                         }

                        if (edText1.getText().toString().trim().isEmpty()){
                            vacioEdit(pos);
                        }
                        else  if ( edText2.getText().toString().trim().isEmpty()){
                            edText2.setText("0");
                        }
                        else if ( edText3.getText().toString().trim().isEmpty()){
                            edText3.setText("0");}



                        final String nombre = edText1.getText().toString();
                        final Float tiempo = Float.valueOf(edText2.getText().toString());
                        final Float distancia = Float.valueOf(edText3.getText().toString());

                        MainActivity.this.arrayDYT.get(pos).setNombre(nombre);
                        MainActivity.this.arrayDYT.get(pos).setTiempo(tiempo);
                        MainActivity.this.arrayDYT.get(pos).setDistancia(distancia);
                        MainActivity.this.items.set(pos,nombre);

                        MainActivity.this.itemsAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();

    }

    private void mostrarStats() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_training_results,null);
        builder.setView(customLayout);

        float kilometrosTotales = 0;
        float minutosPorKilometroTotales = 0;

        for (int x = 0; x < arrayDYT.size(); x++) {
            kilometrosTotales = kilometrosTotales + arrayDYT.get(x).getDistancia();
            minutosPorKilometroTotales = minutosPorKilometroTotales + arrayDYT.get(x).kilometrosPorHora();
        }

        builder.setTitle("Resumen de los entrenamientos");

        final TextView textView = (TextView) customLayout.findViewById(R.id.textViewResult);
        textView.setText(" El total de kilometros recorridos es: " +String.valueOf(kilometrosTotales)+
                " y la media de velocidad es: "+ String.valueOf(minutosPorKilometroTotales));

        builder.setNegativeButton("OK", null);

        builder.create().show();

    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.saveSate();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadState();
    }

    private void saveSate() {
        try (FileOutputStream f = this.openFileOutput( CfgFileName, Context.MODE_PRIVATE ) )
        {
            PrintStream cfg = new PrintStream( f );

            for(Entrenamiento entrenamiento: this.arrayDYT) {
                cfg.println( entrenamiento.getNombre() );
                cfg.println( entrenamiento.getTiempo() );
                cfg.println( entrenamiento.getDistancia() );
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
        try (FileInputStream f = this.openFileInput( CfgFileName  ) )
        {
            BufferedReader cfg = new BufferedReader( new InputStreamReader( f ) );

            this.arrayDYT.clear();
            this.items.clear();
            String line = cfg.readLine();

            while( line != null ) {

                Entrenamiento entrenamiento = new Entrenamiento( line );
                entrenamiento.setTiempo( Float.parseFloat( cfg.readLine() ) );
                entrenamiento.setDistancia( Float.parseFloat( cfg.readLine() ) );
                this.arrayDYT.add( entrenamiento  );
                this.items.add(entrenamiento.getNombre());
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

    private void vacioAdd(){

        final AlertDialog.Builder builderb = new AlertDialog.Builder(this);
        builderb.setTitle("Error");
        builderb.setMessage("No puede haber tareas vacías");
        builderb.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.onAdd();
            }
        });
        builderb.setNegativeButton("Cancelar", null);
        builderb.create().show();
    }

    private void vacioEdit(final int pos){

        final AlertDialog.Builder builderb = new AlertDialog.Builder(this);
        builderb.setTitle("Error");
        builderb.setMessage("No puede haber tareas vacías");
        builderb.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.edit(pos);
            }
        });
        builderb.setNegativeButton("Cancelar", null);
        builderb.create().show();
    }

}