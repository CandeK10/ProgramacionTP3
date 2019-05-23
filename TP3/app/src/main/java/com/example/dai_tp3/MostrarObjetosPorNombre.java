package com.example.dai_tp3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
                Log.d("PorNombre", "Me conecto");
                if(miConexion.getResponseCode() == 200){
                    Log.d("PorNombre", "Conexión OK");
                    InputStream textoRespuesta = miConexion.getInputStream();
                    InputStreamReader LectorRespuesta = new InputStreamReader(textoRespuesta, "UTF-8");

                    ObtenerObjetos(LectorRespuesta);
                }
                else{
                    Log.d("PorNombre", "Error al conectar");
                }
                miConexion.disconnect();
            }catch(Exception error){
                Log.d("PorNombre", "El error es: "+error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            TextView Error;
            Error = findViewById(R.id.MensajeError);
            if(Objetos.isEmpty()){
                Error.setText("No se han encontrado objetos");
            }
            else{
                Error.setText("Los objetos encontrados son:");
                ListaObjetos.setAdapter(Adaptador);
            }
        }
    }

    public void ObtenerObjetos(InputStreamReader LosDatos){
        JsonReader DatosLeidos = new JsonReader(LosDatos);
        try{
            DatosLeidos.beginObject();
            while(DatosLeidos.hasNext()){
                String nombreElementoActual = DatosLeidos.nextName();
                Log.d("NombresLectura", "El elemento actual es: "+nombreElementoActual);

                if(nombreElementoActual.equals("totalFull")){
                    DatosLeidos.skipValue();
                }
                if(nombreElementoActual.equals("clasesEncontradas")){
                    DatosLeidos.skipValue();
                }
                if(nombreElementoActual.equals("instancias")){
                    DatosLeidos.beginArray();
                    while(DatosLeidos.hasNext()){
                        DatosLeidos.beginObject();
                        while (DatosLeidos.hasNext()){
                            nombreElementoActual=DatosLeidos.nextName();
                            if(nombreElementoActual.equals("nombre")){
                                String NombreObjeto = DatosLeidos.nextString();
                                Log.d("NombreSLectura", "Valor leido: "+NombreObjeto);
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
        }catch (Exception error){
            Log.d("NombresLectura", "El error es: "+error.getMessage());
        }
    }
}
