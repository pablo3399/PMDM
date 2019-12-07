package com.example.ejercicio2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.ejercicio2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private int year;
    private int month;
    private int day;
    private int positionF;
    public static final String EtqApp = "to-do";
    public static final String CfgFileName = "droidseries.cfg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.items = new ArrayList<String>();

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
        });}


    @Override public void onResume() {
        super.onResume();
        final ArrayList<String> caducados = new ArrayList<String>();
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
        final ArrayList<String> borrados = new ArrayList<String>();
        final ArrayList<String> info = new ArrayList<String>();
         boolean [] caduca2 = new boolean[items.size()];
        int b = 0;

        final Calendar c = Calendar.getInstance();

        final int mesFin = c.get(Calendar.MONTH) + 1;
        final int diaFin = c.get(Calendar.DAY_OF_MONTH);
        final int anioFin = c.get(Calendar.YEAR);

        String fechaHoy = diaFin + "/" + mesFin + "/" + anioFin;

            for (int a = 0; a < items.size(); a++) {
                String[] parts = items.get(a).trim().split("-");
                String fecha = parts[1];
                if (compararFechas(fecha, fechaHoy) == false) {
                    caducados.add(items.get(a));
                    caduca2[b] = false;
                    b++;
                }

            }

            if(caducados.size()==0){

            }else{
            String[] listItems = new String[caducados.size()];
            for (int x = 0; x < caducados.size(); x++) {

                listItems[x] = caducados.get(x);

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
                                Iterator<String> iteratorItems = items.iterator();
                                while (iteratorItems.hasNext()) {
                                    if (borrados.contains(iteratorItems.next())) {
                                        iteratorItems.remove();
                                    }
                                }
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

        }catch ( ParseException e){
            e.printStackTrace();
        }

        if(fechaDate.before(fechaHoyFormateada)){
            return false;
        }else{
            return true;
        }
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

    private void onAdd() {
        final EditText edText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To-do...");
        builder.setMessage("Que deseas recordar?...");
        builder.setView(edText);

        builder.setPositiveButton("+", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(edText.getText().toString().trim().isEmpty()) {
                    vacioAdd();
                }else {
                    MainActivity.this.fecha(edText);
                }
            }

        }).setNegativeButton("Cancel", null);
        builder.create().show();

    }

    private void fecha(final EditText edText){

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

                        final String date = MainActivity.this.day + "/" + MainActivity.this.month + "/" + MainActivity.this.year;

                        final String text = edText.getText().toString() + " - " + date;


                        MainActivity.this.itemsAdapter.add(text);
                        MainActivity.this.updateStatus();

                    }
                },
                anio, mes, dia
        );
        dlg.show();
    }

    private void updateStatus() {
        TextView txtNum = (TextView) this.findViewById(R.id.lblNum);
        txtNum.setText(Integer.toString(this.itemsAdapter.getCount()));
    }

    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;

    private void edit(final int pos) {
        final TextView textView = new TextView(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Modificar elemento");
        textView.setPadding(64,30,1,1);
        builder.setView(textView);
        textView.setText(MainActivity.this.items.get(pos).toString());
        builder.setPositiveButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final EditText editText = new EditText(MainActivity.this);
                        String[] part = items.get(pos).trim().split("-");
                        String name = part[0];
                        editText.setText(name);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("Modificar tarea");
                        builder1.setMessage("Nueva modificacion:");
                        builder1.setView(editText);

                        builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(editText.getText().toString().trim().isEmpty()) {
                                    vacioEdit(pos);
                                }else {
                                    editFecha(pos, editText);
                                }
                            }
                        });
                        builder1.setNegativeButton("cancel", null);
                        builder1.create().show();
                    }

        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();

    }

    public void editFecha(final int pos, final EditText editText){

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

                        final String text = editText.getText().toString() + " - " + date;

                        MainActivity.this.items.set(pos, text);
                        MainActivity.this.itemsAdapter.notifyDataSetChanged();

                    }
                },
                anio, mes, dia
        );
        dlg.show();

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
        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();
        Set<String> set = new HashSet<String>();
        set.addAll(items);
        edit.putStringSet("key", set);
        edit.commit();
    }

    private void loadState() {
        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        Set<String> set = prefs.getStringSet("key", null);
        items.clear();
        if(set!=null) {
            items.addAll(set);
            MainActivity.this.updateStatus();
        }

    }
}