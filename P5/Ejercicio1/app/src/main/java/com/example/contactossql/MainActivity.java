package com.example.contactossql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contactossql.DBManager;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    protected static final int CODIGO_ADITION_CONTACT = 100;
    protected static final int CODIGO_EDITION_CONTACT = 102;
    protected int positionF;
    protected ArrayList<Integer> positionM = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_main );

        ListView lvLista = this.findViewById( R.id.lvLista );
        ImageButton btInserta = this.findViewById( R.id.btInserta );

        // Inserta
        btInserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzaEditor( 0, "", "", 0, "" );
            }
        });

        this.registerForContextMenu( lvLista );
        this.gestorDB = new DBManager( this.getApplicationContext() );
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // Configurar lista
        final ListView lvLista = this.findViewById( R.id.lvLista );

        this.adaptadorDB = new SimpleCursorAdapter(
                this,
                R.layout.lvlista_contact,
                null,
                new String[] { DBManager.CONTACTO_COL_NOMBRE, DBManager.CONTACTO_COL_APELLIDO, DBManager.CONTACTO_COL_TELEFONO,
                DBManager.CONTACTO_COL_EMAIL},
                new int[] { R.id.lvLista_Contact_Nombre, R.id.lvLista_Contact_Apellido, R.id.lvLista_Contact_Telefono,
                        R.id.lvLista_Contact_Email}
        );

        lvLista.setAdapter( this.adaptadorDB );
        this.actualizaContacto();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        this.gestorDB.close();
        this.adaptadorDB.getCursor().close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu( menu, v, menuInfo );

        if ( v.getId() == R.id.lvLista ) {
            this.getMenuInflater().inflate( R.menu.lista_menu_contextual, menu );
        }

        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        boolean toret = super.onContextItemSelected(item);
        final int pos = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position;
        final  Cursor cursor = this.adaptadorDB.getCursor();

        switch ( item.getItemId() ) {
            case R.id.contact_contextual_elimina:

                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Esta seguro de que quiere eliminar esta tarea?");
                b.setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( cursor.moveToPosition( pos ) ) {
                            final int id = cursor.getInt(0);
                            gestorDB.eliminaContacto(id);
                            actualizaContacto();
                        }
                    }});
                b.setNegativeButton("cancel", null);
                b.create().show();


                break;
            case R.id.contact_contextual_modifica:
                if ( cursor.moveToPosition( pos ) ) {
                    final int id = cursor.getInt(0);
                    final String nombre = cursor.getString( 1 );
                    final String apellido = cursor.getString( 2 );
                    final int telefono = cursor.getInt( 3 );
                    final String email = cursor.getString( 4 );

                    lanzaEditor( id, nombre, apellido, telefono, email );
                    toret = true;
                } else {
                    String msg = this.getString( R.string.msgNoPos ) + ": " + pos;
                    Log.e( "context_modifica", msg );
                    Toast.makeText( this, msg, Toast.LENGTH_LONG ).show();
                }

                break;
        }

        return toret;
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
        Cursor cursor = gestorDB.getContactos();
        final ArrayList<String> listaString= new ArrayList<String>();

        while(cursor.moveToNext()){
            String dato1=cursor.getString((cursor.getColumnIndex(DBManager.CONTACTO_COL_APELLIDO )));
            String dato2=cursor.getString((cursor.getColumnIndex(DBManager.CONTACTO_COL_EMAIL )));
            listaString.add(dato1 + " " + dato2 );
        }

        final String[] listItems = new String[listaString.size()];
        for(int x = 0; x<listaString.size(); x++){

            listItems[x]=listaString.get(x);

        }

        switch( menuItem.getItemId() ) {
            case R.id.contact_options_anhadir:
                lanzaEditor( 0, "", "", 0, "" );
                toret = true;
                break;
            case R.id.contact_options_modifica:

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

                        final  Cursor cursor = adaptadorDB.getCursor();

                        if ( cursor.moveToPosition( positionF ) ) {
                            final int id = cursor.getInt(0);
                            final String nombre = cursor.getString( 1 );
                            final String apellido = cursor.getString( 2 );
                            final int telefono = cursor.getInt( 3 );
                            final String email = cursor.getString( 4 );

                            lanzaEditor( id, nombre, apellido, telefono, email );

                        }

                    }
                });

                getMod.setNegativeButton("cancel",null);

                getMod.create().show();
                toret = true;
                break;

            case R.id.contact_options_elimina:

                AlertDialog.Builder getDel = new AlertDialog.Builder(MainActivity.this);
                getDel.setTitle("Cual quieres eliminar?");

                boolean[] necesitado = new boolean[listaString.size()];

                for(int x = 0; x<listaString.size(); x++){

                    listItems[x]=listaString.get(x);
                    necesitado[x]=false;

                }

                getDel.setMultiChoiceItems(listItems, necesitado, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean b) {

                            positionM.add( which);

                    }
                });

                getDel.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int a = 0 ; a<positionM.size(); a++){

                            final  Cursor cursor = adaptadorDB.getCursor();

                        if ( cursor.moveToPosition( positionM.get(a) ) ) {
                            final int id = cursor.getInt(0);
                            gestorDB.eliminaContacto(id);
                            actualizaContacto();
                            cursor.close();
                        }
                        }
                    }
                });

                getDel.setNegativeButton("cancel",null);

                getDel.create().show();

                toret = true;
                break;
        }

        return toret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent retData)
    {
        if ( resultCode == Activity.RESULT_OK
                && requestCode == CODIGO_ADITION_CONTACT )
        {
            String nombre = retData.getExtras().getString( "nombre", "ERROR" );
            String apellido = retData.getExtras().getString( "apellido", "ERROR" );
            int telefono = retData.getExtras().getInt( "telefono", -1 );
            String email = retData.getExtras().getString( "email", "ERROR" );

            this.gestorDB.insertaContacto( nombre, apellido, telefono, email );
            this.actualizaContacto();
        }

        if( resultCode == Activity.RESULT_OK
                 && requestCode == CODIGO_EDITION_CONTACT)
        {
            int id = retData.getExtras().getInt("id", 0);
            String nombre = retData.getExtras().getString( "nombre", "ERROR" );
            String apellido = retData.getExtras().getString( "apellido", "ERROR" );
            int telefono = retData.getExtras().getInt( "telefono", -1 );
            String email = retData.getExtras().getString( "email", "ERROR" );
            this.gestorDB.modificaContacto(id, nombre, apellido, telefono, email );
            this.actualizaContacto();
        }

        return;
    }

    /** Actualiza el num. de elementos existentes en la vista. */
    private void actualizaContacto()
    {
        final TextView lblNum = this.findViewById( R.id.lblNum );

        this.adaptadorDB.changeCursor( this.gestorDB.getContactos() );
        lblNum.setText( String.format( Locale.getDefault(),"%d", this.adaptadorDB.getCount() ) );
    }

    private void lanzaEditor(int id, String nombre, String apellido, int telefono, String email)
    {
        Intent subActividad = new Intent( MainActivity.this, ContactEditionActivity.class );

        subActividad.putExtra( "id", id );
        subActividad.putExtra( "nombre", nombre );
        subActividad.putExtra( "apellido", apellido );
        subActividad.putExtra( "telefono", telefono );
        subActividad.putExtra( "email", email );

        if(id==0){
            MainActivity.this.startActivityForResult( subActividad, CODIGO_ADITION_CONTACT );
        }else{
            MainActivity.this.startActivityForResult( subActividad, CODIGO_EDITION_CONTACT );
        }
    }

    private DBManager gestorDB;
    private SimpleCursorAdapter adaptadorDB;
}