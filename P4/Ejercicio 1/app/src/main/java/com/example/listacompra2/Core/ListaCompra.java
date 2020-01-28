package com.example.listacompra2.Core;

import android.app.Application;

import com.example.listacompra2.Core.Item;

import java.util.ArrayList;
import java.util.List;

public class ListaCompra extends Application {

    public void onCreate()
    {
        super.onCreate();
        this.items = new ArrayList<>();
    }
    //Recuperar lista
    public List<Item> getItemList() {
        return this.items;
    }
    //Añadir item
    public void addItem(String nombre, int num) {
        Item item = new Item( nombre );
        item.setNum( num );
        this.items.add( item );
    }
    //modificara item mediante su posicion en la lista y modifica su nombre y su cantidad con los datos dados
    public void modifyItem(int pos, String nombre, int num) {
        Item item = new Item( nombre );
        item.setNum( num );
        this.items.set( pos, item );
    }
    private List<Item> items;
    private int pos;

}
