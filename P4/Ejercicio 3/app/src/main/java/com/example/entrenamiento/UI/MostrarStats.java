package com.example.entrenamiento.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.entrenamiento.Core.ListaDeEntrenamientos;
import com.example.entrenamiento.R;

public class MostrarStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_training);

        final Button ok = (Button) this.findViewById(R.id.ok);
        final Button volver = (Button) this.findViewById(R.id.atras);
        final TextView lblStats = (TextView) this.findViewById(R.id.lblStats);
        final TextView lblStats2 = (TextView) this.findViewById(R.id.lblStats2);

        //Declaracion de la clase 'Application' personalizada
        final ListaDeEntrenamientos list = (ListaDeEntrenamientos) this.getApplication();

        float kilometrosTotales = 0;
        float minutosPorKilometroTotales = 0;

        for (int x = 0; x < list.getTrainingList().size(); x++) {
            kilometrosTotales = kilometrosTotales + list.getTrainingList().get(x).getDistancia();
            minutosPorKilometroTotales = minutosPorKilometroTotales + list.getTrainingList().get(x).kilometrosPorHora();
        }

        lblStats.setText(" El total de kilometros recorridos es: " +String.valueOf(kilometrosTotales));

        lblStats2.setText("La media de velocidad es: "+ String.valueOf(minutosPorKilometroTotales));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Manda el resultado a otra actividad
                MostrarStats.this.setResult(Activity.RESULT_OK);
                MostrarStats.this.finish();

            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarStats.this.setResult(Activity.RESULT_CANCELED);
                MostrarStats.this.finish();
            }
        });

    }
}
