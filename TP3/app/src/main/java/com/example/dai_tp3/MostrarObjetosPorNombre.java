package com.example.dai_tp3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MostrarObjetosPorNombre extends AppCompatActivity {
    String Texto;
    ArrayAdapter<String> Adaptador;
    ListView ListaObjetos;
    ArrayList<String> Objetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_objetos_por_nombre);

        Bundle DatosRecibidos;
        DatosRecibidos = this.getIntent().getExtras();

        Texto=DatosRecibidos.getString("Texto");
        Log.d("PorNombre", "El texto ingresado es: "+Texto);

        Objetos = new ArrayList<>();
        Adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Objetos);
        ListaObjetos = findViewById(R.id.ListaDeObjetosPorNombre);

        TareaAsincronica miTarea = new TareaAsincronica();
        miTarea.execute();
    }

    private class TareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String miURL = "http://epok.buenosaires.gob.ar/buscar/?texto=" + Texto;
                URL miRuta = new URL(miURL);
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();


            }catch{

            }
        }
    }
}
