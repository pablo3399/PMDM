package com.example.contactossql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.icu.text.MessagePattern.ArgType.SELECT;

/** Maneja el acceso a la base de datos. */
public class DBManager extends SQLiteOpenHelper {

    public static final String DB_NOMBRE = "ListaContactos";
    public static final int DB_VERSION = 1;

    public static final String TABLA_CONTACTO = "contactos";
    public static final String CONTACTO_COL_ID = "_id";
    public static final String CONTACTO_COL_NOMBRE = "nom";
    public static final String CONTACTO_COL_APELLIDO = "apell";
    public static final String CONTACTO_COL_TELEFONO = "telf";
    public static final String CONTACTO_COL_EMAIL = "email";


    public DBManager(Context context)
    {
        super( context, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(  "DBManager",
                "Creando BBDD " + DB_NOMBRE + " v" + DB_VERSION);

        try {
            db.beginTransaction();
            db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLA_CONTACTO + "( "
                    + CONTACTO_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + CONTACTO_COL_NOMBRE + " string(20) NOT NULL, "
                    + CONTACTO_COL_APELLIDO + " string(50) NOT NULL, "
                    + CONTACTO_COL_TELEFONO + " int(15)  NOT NULL, "
                    + CONTACTO_COL_EMAIL + " string(100)  NOT NULL) ");
            db.setTransactionSuccessful();
        }
        catch(SQLException exc)
        {
            Log.e( "DBManager.onCreate", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(  "DBManager",
                "DB: " + DB_NOMBRE + ": v" + oldVersion + " -> v" + newVersion );

        try {
            db.beginTransaction();
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_CONTACTO );
            db.setTransactionSuccessful();
        }  catch(SQLException exc) {
            Log.e( "DBManager.onUpgrade", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }

    /** Devuelve todas los contactos en la BD
     * @return Un Cursor con los contactos. */
    public Cursor getContactos()
    {
        return this.getReadableDatabase().query( TABLA_CONTACTO,
                null, null, null, null, null, null );
    }

    /** Inserta un nuevo contacto.
     * @param nombre El nombre del contacto.
     * @param apellido El apellido del contacto.
     * @param telefono El telefono del contacto.
     * @param email El email del contacto.
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */


    public void insertaContacto(String nombre, String apellido, int telefono, String email){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;


        values.put( CONTACTO_COL_NOMBRE, nombre );
        values.put( CONTACTO_COL_APELLIDO, apellido );
        values.put( CONTACTO_COL_TELEFONO, telefono );
        values.put( CONTACTO_COL_EMAIL, email);

        try{
                db.beginTransaction();
        cursor=db.query(TABLA_CONTACTO, null, null, null, null,
                null, null );

               db.insert( TABLA_CONTACTO, null, values );

                db.setTransactionSuccessful();}

        catch(SQLException exc)
        {
            Log.e( "DBManager.inserta", exc.getMessage() );
        }
        finally {
            if ( cursor != null ) {
                cursor.close();
            }

            db.endTransaction();
        }
    }

    /** Elimina un elemento de la base de datos
     * @param id El identificador del elemento.
     * @return true si se pudo eliminar, false en otro caso.
     */
    public boolean eliminaContacto(int id)
    {
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete( TABLA_CONTACTO, CONTACTO_COL_ID + "=?", new String[]{ String.valueOf(id) } );
            db.setTransactionSuccessful();
            toret = true;
        } catch(SQLException exc) {
            Log.e( "DBManager.elimina", exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        return toret;
    }

    public void modificaContacto(int id, String nombre, String apellido, int telefono, String email){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;

        values.put( CONTACTO_COL_NOMBRE, nombre );
        values.put( CONTACTO_COL_APELLIDO, apellido );
        values.put( CONTACTO_COL_TELEFONO, telefono );
        values.put( CONTACTO_COL_EMAIL, email);

        try{
            db.beginTransaction();
            cursor=db.query(TABLA_CONTACTO, null, null, null, null,
                    null, null );

            db.update( TABLA_CONTACTO, values, CONTACTO_COL_ID + "=?", new String[]{String.valueOf(id)} );

            db.setTransactionSuccessful();
        }

        catch(SQLException exc)
        {
            Log.e( "DBManager.inserta", exc.getMessage() );
        }
        finally {
            if ( cursor != null ) {
                cursor.close();
            }

            db.endTransaction();
        }

    }
}