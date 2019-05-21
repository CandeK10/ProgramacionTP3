package com.example.dai_tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BuscarPorNombre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_por_nombre);
    }

    public void BuscarPorNombre (View vistaRecibida){
        EditText textoIngresado;
        textoIngresado = findViewById(R.id.TextoIngresado);

        Bundle PaqueteDeDatos;
        PaqueteDeDatos = new Bundle();
        PaqueteDeDatos.putString("Texto", textoIngresado.getText().toString());

        Intent ActividadDestino;
        ActividadDestino = new Intent(BuscarPorNombre.this, MostrarObjetosPorNombre.class);
        ActividadDestino.putExtras(PaqueteDeDatos);

        startActivity(ActividadDestino);
    }
}
