package com.example.listaweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    public static final String DB_NOMBRE = "ListaCompraWeb";
    public static final int DB_VERSION = 1;

    public static final String TABLA_LCOMPRA = "listaCompra";
    public static final String LCOMPRA_COL_NOMBRE = "id_nombre";
    public static final String LCOMPRA_COL_CANTIDAD = "cantidad";


    public DBManager(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBManager",
                "Creando BBDD " + DB_NOMBRE + " v" + DB_VERSION);

        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_LCOMPRA + "( "
                    + LCOMPRA_COL_NOMBRE + " string(30) PRIMARY KEY NOT NULL, "
                    + LCOMPRA_COL_CANTIDAD + " INTEGER NOT NULL) ");

            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onCreate", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DBManager",
                "DB: " + DB_NOMBRE + ": v" + oldVersion + " -> v" + newVersion);

        try {
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_LCOMPRA);
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.onUpgrade", exc.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }

    /**
     * Devuelve todas los contactos en la BD
     *
     * @return Un Cursor con los contactos.
     */
    public Cursor getBD() {
        return this.getReadableDatabase().query(TABLA_LCOMPRA,
                null, null, null, null, null, null);
    }

    /**
     * Inserta un nuevo contacto.
     *
     * @param nombre   El nombre del contacto.
     * @param cantidad El apellido del contacto.
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */


    public void insertarItem(String nombre, int cantidad) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;

        values.put(LCOMPRA_COL_NOMBRE, nombre);
        values.put(LCOMPRA_COL_CANTIDAD, cantidad);

        try {
            db.beginTransaction();
            cursor = db.query(TABLA_LCOMPRA, null, null, null, null,
                    null, null);

            db.insert(TABLA_LCOMPRA, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.inserta", exc.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            db.endTransaction();
        }
    }

    /**
     * Elimina un elemento de la base de datos
     *
     * @param nombre El identificador del elemento.
     * @return true si se pudo eliminar, false en otro caso.
     */
    public boolean eliminarItem(String nombre) {
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete(TABLA_LCOMPRA, LCOMPRA_COL_NOMBRE + "=?", new String[]{nombre});
            db.setTransactionSuccessful();
            toret = true;
        } catch (SQLException exc) {
            Log.e("DBManager.elimina", exc.getMessage());
        } finally {
            db.endTransaction();
        }

        return toret;
    }

    public void modificarItem(String nombreViejo, String nombre, int cantidad) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;

        System.out.println(cantidad);
        values.put(LCOMPRA_COL_NOMBRE, nombre);
        values.put(LCOMPRA_COL_CANTIDAD, cantidad);

        try {
            db.beginTransaction();
            cursor = db.query(TABLA_LCOMPRA, null, null, null, null,
                    null, null);

            db.update(TABLA_LCOMPRA, values, LCOMPRA_COL_NOMBRE + "=?", new String[]{nombreViejo});

            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("DBManager.modify", exc.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            db.endTransaction();
        }

    }

}
