package com.example.dai_tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BuscarPorCategoria(View VistaRecibida){
        Intent ActividadDestino;
        ActividadDestino = new Intent(MainActivity.this, BuscarPorCategoria.class);
        startActivity(ActividadDestino);
    }

    public void BuscarPorNombre (View VistaRecibida){
        Intent ActividadDestino;
        ActividadDestino = new Intent(MainActivity.this, BuscarPorNombre.class);
        startActivity(ActividadDestino);
    }

    public void BuscarPorGeolocalizacion (View VistaRecibida){
        Intent ActividadDestino;
        ActividadDestino = new Intent(MainActivity.this, BuscarPorGeolocalizacion.class);
        startActivity(ActividadDestino);
    }
}
