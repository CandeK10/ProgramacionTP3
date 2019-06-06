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

public class MostrarObjetosPorGeolocalizacion extends AppCompatActivity {
    Float CoordenadaX, CoordenadaY;
    int Radio;
    String Categoria;

    ArrayAdapter Adaptador;
    ArrayList<String> Objetos;
    ListView ListaObjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_objetos_por_geolocalizacion);

        Bundle DatosRecibidos;
        DatosRecibidos = this.getIntent().getExtras();

        CoordenadaX = DatosRecibidos.getFloat("CoordenadaX");
        CoordenadaY = DatosRecibidos.getFloat("CoordenadaY");
        Radio = DatosRecibidos.getInt("Radio");
        Categoria = DatosRecibidos.getString("Categoria");

        Objetos = new ArrayList<>();
        Adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Objetos);
        ListaObjetos = findViewById(R.id.ListaDeObjetosPorGeolocalizacion);

        TareaAsincronica miTarea = new TareaAsincronica();
        miTarea.execute();
    }

    private class TareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids){
            try{
                //Datos para probar: x=58.406818&y=34.621554&categorias=bancos&radio=800
                String miURL = "http://epok.buenosaires.gob.ar/reverseGeocoderLugares/?x=-"+CoordenadaX+"&y=-"+CoordenadaY+"&categorias="+Categoria+"&radio="+Radio;
                URL miRuta = new URL(miURL);
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();
                Log.d("PorGeo", "Me conecto");
                if(miConexion.getResponseCode()==200){
                    Log.d("PorGeo", "Conexion OK");
                    InputStream textoRespuesta = miConexion.getInputStream();
                    InputStreamReader LectorRespuesta = new InputStreamReader(textoRespuesta);

                    ObtenerObjetos(LectorRespuesta);
                }
                else{
                    Log.d("PorGeo", "Error al conectar");
                }
                miConexion.disconnect();
            }catch(Exception error){
                Log.d("PorGeo", "El error es: " + error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            TextView aviso;
            aviso = findViewById(R.id.AvisoObjetosNoEncontradosGeo);
            if(Objetos.isEmpty()){
                aviso.setText("No se han encontrado objetos con los datos ingresados");
            }
            else{
                aviso.setText("Los objetos encontrados son:");
                ListaObjetos.setAdapter(Adaptador);
            }
        }
    }

    public void ObtenerObjetos(InputStreamReader LosDatos){
        JsonReader Datos = new JsonReader(LosDatos);
        try{
            Datos.beginObject();
            while (Datos.hasNext()){
                String ElementoActual = Datos.nextName();
                Log.d("GeoLectura", "El elemento actual es: " + ElementoActual);
                if(ElementoActual.equals("totalFull")){
                    Datos.skipValue();
                }
                if(ElementoActual.equals("total")){
                    Datos.skipValue();
                }
                if(ElementoActual.equals("instancias")){
                    Datos.beginArray();
                    while (Datos.hasNext()){
                        Datos.beginObject();
                        while (Datos.hasNext()){
                            ElementoActual=Datos.nextName();
                            if(ElementoActual.equals("nombre")){
                                String NombreObjeto = Datos.nextString();
                                Log.d("GeoLectura", "Valor leido: "+ NombreObjeto);
                                Objetos.add(NombreObjeto);
                            }
                            else{
                                Datos.skipValue();
                            }
                        }
                        Datos.endObject();
                    }
                    Datos.endArray();
                }
            }
            Datos.endObject();
        }catch (Exception error){
            Log.d("GeoLectura", "El error es: " + error.getMessage());
        }
    }
}
