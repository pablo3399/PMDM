package com.example.entrenamiento.Core;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ListaDeEntrenamientos extends Application{

        public void onCreate()
        {
            super.onCreate();
            this.training = new ArrayList<>();
        }
        //Recuperar lista
        public List<Entrenamiento> getTrainingList() {
            return this.training;
        }
        //Añadir item
        public void addEntrenamiento(String nombre, float tiempo, float distancia) {
            Entrenamiento entrenamiento = new Entrenamiento( nombre );
            entrenamiento.setTiempo( tiempo );
            entrenamiento.setDistancia( distancia );
            this.training.add( entrenamiento );
        }
        //modificara un entrenamiento mediante su posicion en la lista y modifica su nombre y su tiempo y distancia
        public void ModificarEntrenamiento(int pos, String nombre, float tiempo, float distancia) {
            Entrenamiento entrenamiento = new Entrenamiento( nombre );
            entrenamiento.setTiempo( tiempo );
            entrenamiento.setDistancia(distancia);
            this.training.set( pos, entrenamiento );
        }
        private List<Entrenamiento> training;
        private int pos;

}
