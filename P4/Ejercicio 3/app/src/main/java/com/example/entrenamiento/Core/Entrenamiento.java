package com.example.entrenamiento.Core;

import android.app.Application;

public class Entrenamiento extends Application {

    private String nombre;
    private float tiempo;
    private float distancia;

    public Entrenamiento(String nombre, float tiempo, float distancia){

        this.tiempo=tiempo;
        this.distancia = distancia;
        this.nombre = nombre;

    }

    public Entrenamiento(String nombre){

        this.nombre = nombre;
        this.tiempo = 1;
        this.distancia = 1;
    }

    public float minutosPorKilometro (){
        return this.tiempo/this.distancia;
    }

    public float kilometrosPorHora(){
        return this.distancia/(this.tiempo/60);
    }

    @Override
    public String toString() {
        return "Nombre: " +nombre +" ; Tiempo: " + tiempo +" ; Distancia: "+ distancia;
    }

    public String getNombre() {
        return nombre;
    }

    public float getTiempo() {
        return tiempo;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }


}
