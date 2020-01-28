package com.example.listacompra2.View;

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

import com.example.listacompra2.Core.ListaCompra;
import com.example.listacompra2.R;

public class ItemEditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_item_edition );

        final Button btGuardar = (Button) this.findViewById( R.id.btGuardar );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final EditText edCantidad = (EditText) this.findViewById( R.id.edCantidad );
        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        //Declaracion de la clase 'Application' personalizada
        final ListaCompra app = (ListaCompra) this.getApplication();

        Intent datosEnviados = this.getIntent();
        //Recuperacion de la ultima posicion
        final int pos = datosEnviados.getExtras().getInt( "pos" );
        //Nombre por defecto al entrar
        String nombre = "";
        //Cantidad por defecto al entrar
        int cantidad = 1;
        if ( pos >= 0 ) {
            nombre = app.getItemList().get( pos ).getNombre();
            cantidad = app.getItemList().get( pos ).getNum();
        }

        edNombre.setText( nombre );
        edCantidad.setText( Integer.toString( cantidad ) );

        //Metodo para cancelar la agregacion
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemEditionActivity.this.setResult( Activity.RESULT_CANCELED );
                ItemEditionActivity.this.finish();
            }
        });

        //Metodo para guardar los datos añadidos
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    final String nombre = edNombre.getText().toString();
                    final int cantidad = Integer.parseInt( edCantidad.getText().toString() );
                    if ( pos >= 0 ) {
                        app.modifyItem( pos, nombre, cantidad );
                    } else {
                        app.addItem( nombre, cantidad );
                    }
                    //Manda el resultado a otra actividad
                    ItemEditionActivity.this.setResult( Activity.RESULT_OK );
                    ItemEditionActivity.this.finish();
                }
        });

        btGuardar.setEnabled( false );

        edNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled( edNombre.getText().toString().trim().length() > 0 );
            }
        });

        edCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int cantidad = 0;

                try {
                    cantidad = Integer.parseInt( edCantidad.getText().toString() );
                } catch(NumberFormatException exc) {
                    Log.w( "ItemEditionActivity", "edCantidad no puede ser convertido a número" );
                }

                btGuardar.setEnabled( cantidad > 0 );
            }
        });
    }
}