package com.example.p2ii;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private int year;
    private int month;
    private int day;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();

        Button btAdd = (Button) this.findViewById(R.id.btAdd);
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
                MainActivity.this.edit(position);
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.this.onAdd();

            }
        });


    }

    @Override public void onResume(){
        super.onResume();

        final Calendar c = Calendar.getInstance();

        final int mesFin = c.get(Calendar.MONTH)+1;
        final int diaFin = c.get(Calendar.DAY_OF_MONTH) ;
        final int anioFin = c.get(Calendar.YEAR);

        for(int x=0; x<items.size(); x++) {

            System.out.println(items.size());

            String fechaComparar = diaFin + "/" + mesFin +"/"+ anioFin;
            String fechaTarea = MainActivity.this.items.get(x);
            String[] partes = fechaTarea.split(":");
            String fecha = partes[1];
            String nom = partes[0];
            String[] part = nom.split(" /");
            String tarea = part[0];


            System.out.println(fecha);
            System.out.println(fechaComparar);

            if(fecha.equals(fechaComparar)){
                Toast t = Toast.makeText( this, "La actividad: " + tarea + " va a expirar",
                        Toast.LENGTH_LONG);
                t.show();
            }else{

            }

        }

    }

    private void onAdd() {
        final EditText edText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To-do...");
        builder.setMessage("Que deseas recordar?...");
        builder.setView(edText);

        builder.setPositiveButton("+", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MainActivity.this.fecha(edText);

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


    private void edit(final int pos) {
        final EditText edText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Modificar elemento");
        builder.setView(edText);
        edText.setText(MainActivity.this.items.get(pos).toString());
        builder.setPositiveButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String text = edText.getText().toString();
                        String comprobar = edText.getText().toString();
                        String[] partes = comprobar.split(":");
                        String fecha = partes[1];
                        String[] fechaPartes = fecha.split("/");
                        int dia = Integer.valueOf(fechaPartes[0]);
                        int mes = Integer.valueOf(fechaPartes[1]);

                        if (dia < 1 || dia > 31 || mes < 1 || mes > 12) {
                            System.out.println(R.string.cambio);
                        } else {
                        MainActivity.this.items.set(pos, text);
                    }
                }

                });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();

    }

    private void fecha(final EditText edText){

        if(edText.getText().toString().isEmpty()){
            edText.setText(R.string.tareaVacia);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Calendar c = Calendar.getInstance();

        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);

        DatePickerDialog dlg = new DatePickerDialog(MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker dp, int y, int m, int d) {

                            MainActivity.this.year = y;
                            MainActivity.this.month = m + 1;
                            MainActivity.this.day = d;

                            final String date = MainActivity.this.day+ "/" + MainActivity.this.month +"/"+ MainActivity.this.year;

                            final String text = edText.getText().toString() +" /"+ " fecha:"+ date;

                            MainActivity.this.itemsAdapter.add(text);
                            MainActivity.this.updateStatus();

                        }
                    },
                    anio, mes, dia
            );
            dlg.show();

        }

}