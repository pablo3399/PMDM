package com.example.entrenamiento.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.entrenamiento.Core.ListaDeEntrenamientos;
import com.example.entrenamiento.R;

public class AddTraining extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_training);

        final Button aceptar = (Button) this.findViewById( R.id.guardar );
        final Button cancelar = (Button) this.findViewById( R.id.cancelar );
        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        final EditText edTiempo = (EditText) this.findViewById( R.id.edTiempo );
        final EditText edDistancia = (EditText) this.findViewById( R.id.edDistancia );

        //Declaracion de la clase 'Application' personalizada
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplication();



        Intent datosEnviados = this.getIntent();
        //Recuperacion de la ultima posicion
        final int pos = datosEnviados.getExtras().getInt( "pos" );
        //Nombre por defecto al entrar
        String nombre = "";
        //Tiempo por defecto al entrar
        float tiempo = (1);
        //Distancia por defecto al entrar
        float distancia = (1);

        if ( pos >= 0 ) {
            nombre = list.getTrainingList().get( pos ).getNombre();
            tiempo = list.getTrainingList().get(pos).getTiempo();
            distancia = list.getTrainingList().get(pos).getDistancia();
            list.ModificarEntrenamiento(pos, nombre, tiempo, distancia);
        }

        edNombre.setText( nombre );
        edTiempo.setText( String.valueOf(tiempo) );
        edDistancia.setText( String.valueOf(distancia ));

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddTraining.this.setResult( Activity.RESULT_CANCELED );
                AddTraining.this.finish();

            }
        });

        //metodo para agregar o modificar un entrenamiento
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nombre = edNombre.getText().toString();
                 float tiempoF = 0;
                 float distanciaF = 0;
                try{
                 tiempoF = Float.parseFloat(edTiempo.getText().toString());
                 distanciaF = Float.parseFloat(edDistancia.getText().toString());
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }


                if ( pos >= 0 ) {
                    list.ModificarEntrenamiento( pos, nombre, tiempoF, distanciaF );
                } else {
                    list.addEntrenamiento( nombre, tiempoF, distanciaF );
                }

                //Manda el resultado a otra actividad
                AddTraining.this.setResult( Activity.RESULT_OK );
                AddTraining.this.finish();

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


        edTiempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                float tiempo = 0;

                try {
                    tiempo = Float.parseFloat( edTiempo.getText().toString() );
                } catch(NumberFormatException exc) {
                    Log.w( "AddTraining", "edTiempo no puede ser convertido a float" );
                }

                aceptar.setEnabled( tiempo > 0 );

            }
        });

        edDistancia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                float distancia = 0;

                try {
                    distancia = Float.parseFloat( edDistancia.getText().toString() );
                } catch(NumberFormatException exc) {
                    Log.w( "AddTraining", "edDistancia no puede ser convertido a float" );
                }

                aceptar.setEnabled( distancia > 0 );

            }
        });

    }
}
