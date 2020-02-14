package com.example.contactossql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.regex.Pattern;

public class ContactEditionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_contact_edition);

        final ImageButton btGuardar = this.findViewById(R.id.btGuardar);
        final ImageButton btCancelar = this.findViewById(R.id.btCancelar);
        final EditText edNombre = this.findViewById(R.id.edNombre);
        final EditText edApellido = this.findViewById(R.id.edApellido);
        final EditText edTelefono = this.findViewById(R.id.edTelefono);
        final EditText edEmail = this.findViewById(R.id.edEmail);

        InputFilter[] limiteNombre = new InputFilter[1];
        limiteNombre[0] = new InputFilter.LengthFilter(20);
        InputFilter[] limiteApellido = new InputFilter[1];
        limiteApellido[0] = new InputFilter.LengthFilter(50);
        InputFilter[] limiteTelefono = new InputFilter[1];
        limiteTelefono[0] = new InputFilter.LengthFilter(15);
        InputFilter[] limiteEmail = new InputFilter[1];
        limiteEmail[0] = new InputFilter.LengthFilter(100);

        edNombre.setFilters(limiteNombre);
        edApellido.setFilters(limiteApellido);
        edTelefono.setFilters(limiteTelefono);
        edEmail.setFilters(limiteEmail);

        final Intent datosEnviados = this.getIntent();
        final int id = datosEnviados.getExtras().getInt("id", 0);
        final String nombre = datosEnviados.getExtras().getString("nombre", "ERROR");
        final String apellido = datosEnviados.getExtras().getString("apellido", "ERROR");
        final int telefono = datosEnviados.getExtras().getInt("telefono", 666);
        final String email = datosEnviados.getExtras().getString("email", "ERROR");


        edNombre.setText(nombre);
        edApellido.setText(apellido);
        if (telefono != 0) {
            edTelefono.setText(String.valueOf(telefono));
        } else {
            edTelefono.setText("");
        }

        edEmail.setText(email);

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactEditionActivity.this.setResult(Activity.RESULT_CANCELED);
                ContactEditionActivity.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final boolean comprobar = validarEmail(edEmail.getText().toString().trim());
                boolean noParseo = true;
                try{
                    Integer.parseInt(edTelefono.getText().toString());
                }catch(NumberFormatException e){
                    noParseo=false;
                }

                if (edNombre.getText().toString().trim().isEmpty()
                        || edApellido.getText().toString().trim().isEmpty()
                        || edTelefono.getText().toString().trim().isEmpty()
                        || edEmail.getText().toString().trim().isEmpty() || comprobar ==false
                || noParseo==false) {

                    AlertDialog.Builder err = new AlertDialog.Builder(ContactEditionActivity.this);

                    if (edNombre.getText().toString().trim().isEmpty()) {

                        err.setTitle("Error, campo nombre vacío");
                    } else if (edApellido.getText().toString().trim().isEmpty()) {
                        err.setTitle("Error, campo apellido vacío");
                    } else if (edTelefono.getText().toString().trim().isEmpty()) {
                        err.setTitle("Error, campo telefono vacío");
                    } else if (edEmail.getText().toString().trim().isEmpty()) {
                        err.setTitle("Error, campo email vacío");
                    }else if(comprobar == false) {
                        err.setTitle("Campo email no válido");
                    }else if(noParseo == false) {
                    err.setTitle("Campo telefono no válido");
                }

                    err.setNeutralButton("OK", null);
                    err.create().show();

                } else {

                    final String nombre = edNombre.getText().toString();
                    final String apellido = edApellido.getText().toString();
                    final int telefono = Integer.parseInt(edTelefono.getText().toString());
                    final String email = edEmail.getText().toString();

                    final Intent retData = new Intent();

                    if (id > 0) {
                        retData.putExtra("id", id);
                        retData.putExtra("nombre", nombre);
                        retData.putExtra("apellido", apellido);
                        retData.putExtra("telefono", telefono);
                        retData.putExtra("email", email);
                    } else {
                        retData.putExtra("nombre", nombre);
                        retData.putExtra("apellido", apellido);
                        retData.putExtra("telefono", telefono);
                        retData.putExtra("email", email);
                    }
                    ContactEditionActivity.this.setResult(Activity.RESULT_OK, retData);
                    ContactEditionActivity.this.finish();
                }
            }
        });

}
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;

        return pattern.matcher(email).matches();
    }
}