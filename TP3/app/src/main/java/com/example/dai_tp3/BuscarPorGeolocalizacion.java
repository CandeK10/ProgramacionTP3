package com.example.dai_tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BuscarPorGeolocalizacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_por_geolocalizacion);
    }

    public void BuscarPorGeolocalizacion(View vistaRecibida){
        EditText CoordenadaX, CoordenadaY, Radio;
        CoordenadaX=findViewById(R.id.CoordenadaX);
        CoordenadaY=findViewById(R.id.CoordenadaY);
        Radio=findViewById(R.id.Radio);

        String sCoordenadaX, sCoordenadaY, sRadio;
        sCoordenadaX=CoordenadaX.getText().toString();
        sCoordenadaY=CoordenadaY.getText().toString();
        sRadio=Radio.getText().toString();

        if(sCoordenadaX.equals("") || sCoordenadaY.equals("") || sRadio.equals("")){
            TextView MensajeError;
            MensajeError=findViewById(R.id.MensajeErrorPunto3);
            MensajeError.setText("Ingrese todos los datos");
        }else{
            Float fCoordenadaX, fCoordenadaY;
            Integer iRadio;
            fCoordenadaX=Float.parseFloat(sCoordenadaX);
            fCoordenadaY=Float.parseFloat(sCoordenadaY);
            iRadio= Integer.parseInt(sRadio);

            Bundle PaqueteDeDatos;
            PaqueteDeDatos = new Bundle();
            PaqueteDeDatos.putFloat("CoordenadaX", fCoordenadaX);
            PaqueteDeDatos.putFloat("CoordenadaY", fCoordenadaY);
            PaqueteDeDatos.putInt("Radio", iRadio);

            Intent ActividadDestino;
            ActividadDestino = new Intent(BuscarPorGeolocalizacion.this, MostrarObjetosPorGeolocalizacion.class);
            ActividadDestino.putExtras(PaqueteDeDatos);

            startActivity(ActividadDestino);
        }
    }
}
