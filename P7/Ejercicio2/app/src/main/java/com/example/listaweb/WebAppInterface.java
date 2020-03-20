package com.example.listaweb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class WebAppInterface {

    Context context;
    String nombre;
    int cantidad;
    int positionF;
    boolean bol = true;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        this.context = c;
        this.gestorDB = new DBManager(context);
        BDToList();
    }

    /** Show a toast from the web page - muestra un mensaje desde la interfaz web
     */

    @JavascriptInterface
    public void sendData(String data) {
        //Get the string value to process

        String datos[] = data.split(";");
        this.nombre=datos[0];
        try {
            this.cantidad = Integer.valueOf(datos[1]);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }

         for (int l = 0; l<listaItems.size(); l++) {
            System.out.println(listaItems.get(l).getNombre() + "   funchionaaaaaaaaaaaaaaaaaaaaa");

         if(nombre.equals(listaItems.get(l).getNombre())){
          bol=false;
         }}

         if(bol) {
             Item item = new Item(nombre, cantidad);
             this.gestorDB.insertarItem(item.getNombre(), item.getCantidad());
             listaItems.add(item);
         }

        gestorDB.close();
    }

    @JavascriptInterface
    public String getNombres(){

         nombres="";
        for(int x = 0; x<listaItems.size(); x++){
            nombres+=listaItems.get(x).getNombre() + ";";
        }
        return nombres;
    }

    @JavascriptInterface
    public void eleccionEdit(){

        String[] listItems = new String[listaItems.size()];

        for(int x = 0; x<listaItems.size(); x++){
            listItems[x]=listaItems.get(x).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final int checkedItem = 0;

        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println(which);
                positionF=which;
            }
        });

        builder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Edit(positionF);
            }
        });

        builder.setNegativeButton("cancel",null);

        builder.create().show();
    }

    @JavascriptInterface
    public void delete(int pos){
        String borrar = listaItems.get(pos).getNombre();

        listaItems.remove(pos);

        gestorDB.eliminarItem(borrar);

    }

    public void setListaItems(ArrayList<Item> lista){
        this.listaItems = lista;
    }


    public void Edit(int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        final View customLayout = mInflater.inflate(R.layout.dialog_edit,null);
        builder.setView(customLayout);

        final EditText edNombre = (EditText) customLayout.findViewById(R.id.nombre);
        final EditText edCantidad = (EditText) customLayout.findViewById(R.id.cantidad);
        final String nombreV = listaItems.get(position).getNombre();
        cantidad= listaItems.get(position).getCantidad();
        edNombre.setText(listaItems.get(position).getNombre());
        edCantidad.setText(String.valueOf(listaItems.get(position).getCantidad()));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gestorDB.modificarItem(nombreV, edNombre.getText().toString(), Integer.valueOf(edCantidad.getText().toString()));
                cantidad=0;
                positionF=0;
                listaItems.clear();
                BDToList();
                Toast.makeText(context, "Recargue la página para mostrar los cambios relizados", Toast.LENGTH_LONG);
            }
        });

        builder.setNegativeButton("cancel",null);

        builder.create().show();

    }

    @JavascriptInterface
    public int getLoops(){
        return listaItems.size();
    }

    @JavascriptInterface
    public boolean getBoolean(){
        return bol;
    }

    @JavascriptInterface
    public String getCantidades(){

        cantidades="";
        for(int x = 0; x<listaItems.size(); x++){
            cantidades+= listaItems.get(x).getCantidad()+ ";";
        }
        return cantidades;
    }

    public void BDToList(){
        boolean toret = false;

        Cursor cursor = this.gestorDB.getBD();

        while(cursor.moveToNext()){
            String dato1=cursor.getString((cursor.getColumnIndex(DBManager.LCOMPRA_COL_NOMBRE )));
            int dato2=cursor.getInt((cursor.getColumnIndex(DBManager.LCOMPRA_COL_CANTIDAD )));
            Item item = new Item(dato1, dato2);
            listaItems.add(item);
        }

    }

    private DBManager gestorDB;
    private ArrayList<Item> listaItems = new ArrayList<Item>();
    private String cantidades;
    private String nombres;
}
