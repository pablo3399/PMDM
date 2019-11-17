package com.example.p2iii;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();
        this.arrayDYT = new ArrayList<Entrenamiento>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
        Button btStats = (Button) this.findViewById(R.id.btStats);
        ListView lvItems = (ListView) this.findViewById(R.id.lvItems);

        lvItems.setLongClickable(true);
        this.itemsAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_selectable_list_item,
                this.items
        );
        lvItems.setAdapter(this.itemsAdapter);


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                if (pos >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Estas seguro de que quieres borrarlo?");
                    builder.setPositiveButton("no", null);
                    builder.setNegativeButton("si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.this.items.remove(pos);
                                    MainActivity.this.arrayDYT.remove(pos);
                                    MainActivity.this.itemsAdapter.notifyDataSetChanged();
                                    MainActivity.this.updateStatus();
                                }

                            }
                    );
                    builder.create().show();
                }
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MainActivity.this.detalles(position);

            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();

            }
        });


        btStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.mostrarStats();

            }
        });


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

                if (edText1.getText().toString().isEmpty()){
                    edText1.setError("Error");
                }
                else if(edText2.getText().toString().isEmpty()){
                    edText2.setError("Error");
                }
                else if(edText3.getText().toString().isEmpty()){
                    edText3.setError("Error");
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

                        if (edText1.getText().toString().isEmpty()){
                            edText1.setText("Campo vacio");
                        }
                        if(edText2.getText().toString().isEmpty()){
                            edText2.setText("0");
                        }
                        if(edText3.getText().toString().isEmpty()) {
                            edText3.setText("0");
                        }

                        final String nombre = edText1.getText().toString();
                        final Float tiempo = Float.valueOf(edText2.getText().toString());
                        final Float distancia = Float.valueOf(edText3.getText().toString());

                        MainActivity.this.arrayDYT.get(pos).setNombre(nombre);
                        MainActivity.this.arrayDYT.get(pos).setTiempo(tiempo);
                        MainActivity.this.arrayDYT.get(pos).setDistancia(distancia);

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
}