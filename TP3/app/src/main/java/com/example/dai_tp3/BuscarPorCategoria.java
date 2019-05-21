package com.example.dai_tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BuscarPorCategoria extends AppCompatActivity {
    ArrayAdapter<String> Adaptador;
    ListView ListaCategorias;
    ArrayList<String> Categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_por_categoria);

        Categorias = new ArrayList<>();

        Adaptador=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Categorias);
        ListaCategorias = findViewById(R.id.ListaDeCategorias);


        tareaAsincronica miTarea = new tareaAsincronica();
        miTarea.execute();
    }

    public void BuscarCategoria (View VistaRecibida){

        int PosicionSeleccionada;
        PosicionSeleccionada= ListaCategorias.getSelectedItemPosition();

        String CategoriaElegida;
        CategoriaElegida=(String) ListaCategorias.getItemAtPosition(PosicionSeleccionada);

        Bundle PaqueteDeDatos;
        PaqueteDeDatos=new Bundle();
        PaqueteDeDatos.putString("Categoria", CategoriaElegida);

        Intent ActividadDestino;
        ActividadDestino= new Intent(BuscarPorCategoria.this, MostrarObjetosPorCategoria.class);
        ActividadDestino.putExtras(PaqueteDeDatos);

        startActivity(ActividadDestino);


    }

    private class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL miRuta = new URL("http://epok.buenosaires.gob.ar/getCategorias");
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();

                Log.d("AccesoAPI", "Me conecto");
                if (miConexion.getResponseCode() == 200) {
                    Log.d("AccesoAPI", "Conexion OK");
                    InputStream textoRespuesta = miConexion.getInputStream();
                    InputStreamReader LectorRespuesta = new InputStreamReader(textoRespuesta, "UTF-8");

                    ObtenerCategorias(LectorRespuesta);
                }
                else{
                    Log.d("AccesoAPI", "Error en la conexion");
                }
                miConexion.disconnect();
            } catch (Exception error) {
                Log.d("AccesoAPI", "Hubo un error al conectarme: "+error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            ListaCategorias.setAdapter(Adaptador);
        }
    }

    public void ObtenerCategorias (InputStreamReader Datos){
        JsonReader DatosLeidos = new JsonReader(Datos);
        Log.d("LecturaJSON", "Los datos son:      "+DatosLeidos);
        try{
            //VER EL ERROR DEL LOG
            String nombreElementoActual = DatosLeidos.nextName();
            Log.d("LecturaJSON", "El nombre del elemento actual es: "+nombreElementoActual);

            if(nombreElementoActual.equals("cantidad_de_categorias")){

            }
            else{
                DatosLeidos.beginArray();
                while(DatosLeidos.hasNext()){
                    DatosLeidos.beginObject();
                    while(DatosLeidos.hasNext()){
                        nombreElementoActual=DatosLeidos.nextName();
                        if(nombreElementoActual.equals("nombre")){
                            String valorElementoActual = DatosLeidos.nextString();
                            Log.d("LecturaJSON", "Valor leido: "+ valorElementoActual);
                            Categorias.add(valorElementoActual);
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
        catch (Exception error){
            Log.d("LecturaJSON", "El error es: " + error.getMessage());
        }
    }
}
