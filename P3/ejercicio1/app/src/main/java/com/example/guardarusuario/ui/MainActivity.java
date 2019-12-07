package com.example.guardarusuario.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.guardarusuario.R;
import com.example.guardarusuario.core.DatosPersonalesExtendidos;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        final EditText edApellido = (EditText) this.findViewById(R.id.edApellido);
        final EditText edEmail = (EditText) this.findViewById( R.id.edEmail );
        final EditText edDireccion = (EditText) this.findViewById( R.id.edDireccion );
        final EditText edTelefono = (EditText) this.findViewById(R.id.edTelefono);


        edNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersonalesExtendidos.setNombre( edNombre.getText().toString() );
            }
        });

        edApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersonalesExtendidos.setApellido( edApellido.getText().toString() );
            }
        });

        edDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersonalesExtendidos.setDireccion( edDireccion.getText().toString() );
            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.datosPersonalesExtendidos.setEmail( edEmail.getText().toString() );
            }

        });

        edTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    MainActivity.this.datosPersonalesExtendidos.setTelefono(Integer.parseInt(edTelefono.getText().toString()));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );
        String nombre = prefs.getString( "nombre", "" );
        String apellido = prefs.getString("apellido","");
        String direccion = prefs.getString( "direccion", "" );
        String email = prefs.getString( "email", "" );
        int telefono = prefs.getInt( "telefono", 0 );

        this.datosPersonalesExtendidos= new DatosPersonalesExtendidos(nombre,direccion,email,telefono,apellido);

        final EditText edNombre = (EditText) this.findViewById( R.id.edNombre );
        final EditText edApellido = (EditText) this.findViewById(R.id.edApellido);
        final EditText edEmail = (EditText) this.findViewById( R.id.edEmail );
        final EditText edDireccion = (EditText) this.findViewById( R.id.edDireccion );
        final EditText edTelefono = (EditText) this.findViewById(R.id.edTelefono);

        edNombre.setText( this.datosPersonalesExtendidos.getNombre() );
        edApellido.setText(this.datosPersonalesExtendidos.getApellido());
        edDireccion.setText( this.datosPersonalesExtendidos.getDireccion() );
        edEmail.setText( this.datosPersonalesExtendidos.getEmail() );
        if(!edTelefono.getText().toString().isEmpty()){
            edTelefono.setText(String.valueOf(this.datosPersonalesExtendidos.getTelefono()));;}
        else{edTelefono.setText(" ");}


    }


    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences.Editor edit = this.getPreferences( Context.MODE_PRIVATE ).edit();

        edit.putString( "nombre", this.datosPersonalesExtendidos.getNombre());
        edit.apply();
        edit.putString( "apellido", this.datosPersonalesExtendidos.getApellido());
        edit.apply();
        edit.putString( "direccion", this.datosPersonalesExtendidos.getDireccion());
        edit.apply();
        edit.putString( "email", this.datosPersonalesExtendidos.getEmail());
        edit.apply();
        edit.putInt( "telefono", this.datosPersonalesExtendidos.getTelefono());
        edit.apply();

    }

    private DatosPersonalesExtendidos datosPersonalesExtendidos;
}