package com.example.dai_tp3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MostrarObjetosPorCategoria extends AppCompatActivity {
    String Categoria;
    ArrayAdapter<String> Adaptador;
    ListView ListaObjetos;
    ArrayList<String> Objetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_objetos_por_categoria);

        Bundle DatosRecibidos;
        DatosRecibidos=this.getIntent().getExtras();

        Categoria=DatosRecibidos.getString("Categoria");
        Log.d("ListaObjetos", "La categoria es: "+Categoria);

        Objetos = new ArrayList<>();
        Adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Objetos);
        ListaObjetos = findViewById(R.id.ListaDeObjetosPorCategoria);

        TareaAsincronica miTarea = new TareaAsincronica();
        miTarea.execute();
    }


    private class TareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids){
            try{
                String miURL = "http://epok.buenosaires.gob.ar/buscar/?texto=" + Categoria;
                URL miRuta = new URL(miURL);
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();

                Log.d("BuscarObjetos", "Me conecto");
                if(miConexion.getResponseCode()==200){
                    Log.d("BuscarObjetos", "Se conectó");
                    InputStream textoRespuesta = miConexion.getInputStream();
                    InputStreamReader LectorRespuesta = new InputStreamReader(textoRespuesta);

                    ObtenerObjetos (LectorRespuesta);
                }
                else{
                    Log.d("BuscarObjetos", "Error al conectar");
                }
                miConexion.disconnect();
            }catch (Exception error){
                Log.d("BuscarObjetos", "El error es: "+error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            ListaObjetos.setAdapter(Adaptador);
        }
    }

    public void ObtenerObjetos (InputStreamReader LosDatiños){
        JsonReader DatosLeidos = new JsonReader(LosDatiños);
        try{
            DatosLeidos.beginObject();
            while(DatosLeidos.hasNext()){
                String nombreElementoActual = DatosLeidos.nextName();
                Log.d("BuscarObjetos", "El nombre actual es: "+nombreElementoActual);

                if(nombreElementoActual.equals("totalFull")){
                    int totalFull=DatosLeidos.nextInt();
                    Log.d("BuscarObjetos", "La cantidad de objetos es: " + totalFull);
                }
                if(nombreElementoActual.equals("clasesEncontradas")){
                    DatosLeidos.skipValue();
                }
                if(nombreElementoActual.equals("instancias")){
                    DatosLeidos.beginArray();
                    while(DatosLeidos.hasNext()){
                        DatosLeidos.beginObject();
                        while(DatosLeidos.hasNext()){
                            nombreElementoActual=DatosLeidos.nextName();
                            if(nombreElementoActual.equals("nombre")){
                                String NombreObjeto = DatosLeidos.nextString();
                                Log.d("BuscarObjetos", "Valor Leido: "+NombreObjeto);
                                Objetos.add(NombreObjeto);
                            }
                            else{
                                DatosLeidos.skipValue();
                            }
                        }
                        DatosLeidos.endObject();
                    }
                    DatosLeidos.endArray();
                }
            }
            DatosLeidos.endObject();
        }catch(Exception error){
            Log.d("BuscarObjetos", "El error es: " + error.getMessage());
        }
    }
}
