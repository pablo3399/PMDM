package com.example.to_do.Core;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class AppList extends Application {

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
    public void addItem(String nombre, String fecha) {
        Item item = new Item( nombre );
        item.setFecha( fecha );
        this.items.add( item );
    }
    //modificara item mediante su posicion en la lista y modifica su nombre y su cantidad con los datos dados
    public void modifyItem(int pos, String nombre, String fecha) {
        Item item = new Item( nombre );
        item.setFecha( fecha );
        this.items.set( pos, item );
    }
    private List<Item> items;
    private int pos;
}
