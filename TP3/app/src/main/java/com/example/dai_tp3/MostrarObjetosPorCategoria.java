package com.example.dai_tp3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URL;

public class MostrarObjetosPorCategoria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_objetos_por_categoria);

        Bundle DatosRecibidos;
        DatosRecibidos=this.getIntent().getExtras();

        String Categoria;
        Categoria=DatosRecibidos.getString("Categoria");

    }

    /*
    private class TareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void DoInBackground(Void... voids){
            return null;
        }
    }
    */



}
