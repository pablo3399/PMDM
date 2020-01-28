package com.example.to_do.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_do.Core.AppList;
import com.example.to_do.R;

import java.util.Calendar;

public class Add extends AppCompatActivity {

    private int resultado = 0;
    private String fecha;
    private String fechaA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        final Calendar c = Calendar.getInstance();
        final int mesFin = c.get(Calendar.MONTH);
        final int diaFin = c.get(Calendar.DAY_OF_MONTH);
        final int anioFin = c.get(Calendar.YEAR);

        final Button aceptar = (Button) this.findViewById( R.id.guardar );
        final Button cancelar = (Button) this.findViewById( R.id.cancelar );
        final Button btFecha = (Button) this.findViewById( R.id.edFecha );
        final TextView tvFecha = (TextView) this.findViewById(R.id.lblFecha);
        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        //Declaracion de la clase 'Application' personalizada
        final AppList app = (AppList) this.getApplication();

        fecha=diaFin + "/" + (mesFin+1) + "/" + anioFin;

        tvFecha.setText("Fecha: " + diaFin + "/" + (mesFin+1) + "/" + anioFin );

        //boton para agregar fecha por datepicker
        btFecha.setOnClickListener(new View.OnClickListener(){

            @Override
                    public void onClick(View v){

            DatePickerDialog dlg = new DatePickerDialog(Add.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker dp, int y, int m, int d) {

                            final String date = d + "/" + (m+1) + "/" + y;
                            fecha = date;

                            tvFecha.setText("Fecha: "+ date);

                        }
                    },
                    anioFin, mesFin, diaFin
            );
            dlg.show();
        }});


        Intent datosEnviados = this.getIntent();
        //Recuperacion de la ultima posicion
        final int pos = datosEnviados.getExtras().getInt( "pos" );
        //Nombre por defecto al entrar
        String nombre = "";
        //Cantidad por defecto al entrar
         fechaA = diaFin + "/" + (mesFin+1) + "/" + anioFin;
        if ( pos >= 0 ) {
            nombre = app.getItemList().get( pos ).getNombre();
            fechaA = app.getItemList().get(pos).getFecha();
            app.modifyItem(pos, nombre,fechaA);
        }

        edNombre.setText( nombre );
        tvFecha.setText("Fecha: "+ fechaA);

        //en caso de que sea modificar, setea la fecha por defecto a la de la tarea a modificar y en caso de que la quiera
        //cambiar inicia desde el dia que tenia antes
        if(pos>=0){

            fecha=fechaA;

        btFecha.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                String parts[] = fechaA.trim().split("/",3);

                final int part0 = Integer.parseInt(parts[0]);
                final int part1 = Integer.parseInt(parts[1])-1;
                final int part2 = Integer.parseInt(parts[2]);

                DatePickerDialog dlg = new DatePickerDialog(Add.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int y, int m, int d) {

                                final String date = d + "/" + (m+1) + "/" + y;
                                fecha = date;

                                tvFecha.setText("Fecha: "+ date);

                            }
                        },
                        part2, part1, part0
                );
                dlg.show();
                if(fecha!=fechaA){
                    aceptar.setEnabled(true);
                }
            }});}


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Manda el resultado a otra actividady finaliza esta
                Add.this.setResult( Activity.RESULT_CANCELED );
                Add.this.finish();

            }
        });


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nombre = edNombre.getText().toString();

                if ( pos >= 0 ) {
                    app.modifyItem( pos, nombre, fecha );
                } else {
                    app.addItem( nombre, fecha );
                }
                //Manda el resultado a otra actividad y finaliza esta
                Add.this.setResult( Activity.RESULT_OK );
                Add.this.finish();

            }
        });

        aceptar.setEnabled(false);

        //de aqui en adelante, son metodos que evitan que la persona deje en blanco algun campo
        edNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                aceptar.setEnabled(edNombre.getText().toString().trim().length() > 0);
            }
        });

        btFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                aceptar.setEnabled(btFecha.getText().toString().trim().length() > 0);

            }
        });

    }
}



