package com.example.to_do.Core;

import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item extends Application {

    private String nombre;
    private String fecha;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaDate=null;

    public Item(String n)
    {
        this.nombre = n;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String setNombre (String nombre){ return this.nombre = nombre;}

    public String setFecha (String fecha) { return this.fecha = fecha;}

    public Date getFechaDate(){
        try{
        fechaDate = formato.parse(fecha);

    }catch ( ParseException e){
        e.printStackTrace();
    }
     return fechaDate;
    }

    public Date setFechaDate(Date fecha){
        return this.fechaDate=fecha;
    }

    @Override
    public String toString()
    {
        return this.getNombre() + ". ; Fecha: " + this.getFecha();
    }


    public String Date()
    {
        return String.valueOf(this.getFechaDate());
    }

}
