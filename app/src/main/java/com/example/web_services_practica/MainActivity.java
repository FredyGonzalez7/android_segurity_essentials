package com.example.web_services_practica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edicod,edipro,edipre,edifab;
    Button botagr, botbus, botedi, boteli;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edicod = (EditText) findViewById(R.id.edicod);
        edipro = (EditText) findViewById(R.id.edipro);
        edipre = (EditText) findViewById(R.id.edipre);
        edifab = (EditText) findViewById(R.id.edifab);
        botagr = (Button) findViewById(R.id.botagr);
        botbus = (Button) findViewById(R.id.botbus);
        botedi = (Button) findViewById(R.id.botedi);
        boteli = (Button) findViewById(R.id.boteli);

        //final String http="http://192.168.1.10:8080"; //casa
        final String http="http://192.168.1.24:8080"; //Secre

        botagr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ejecutarServicio("http://192.168.1.24:80/web_services_practice/insertar_producto.php");
                ejecutarServicio(""+http+"/web_services_practice/insertar_producto.php");
            }
        });
        botbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buscarProducto("http://192.168.1.24:8080/web_services_practice/buscar_producto.php?codigo="+edicod.getText()+"");
                //buscarProducto1("http://192.168.1.24:8080/web_services_practice/buscar_producto.php");
                buscarProducto1(""+http+"/web_services_practice/buscar_producto.php");
                //buscarProducto2("http://pastebin.com/raw/Em972E5s");
            }
        });
        botedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio(""+http+"/web_services_practice/editar_producto.php");
            }
        });
        boteli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto(""+http+"/web_services_practice/eliminar_producto.php");
            }
        });
    }
        //metodo que envia las peticiones PHP al servidor
    private void ejecutarServicio(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codigo",edicod.getText().toString());
                parametros.put("producto",edipro.getText().toString());
                parametros.put("precio",edipre.getText().toString());
                parametros.put("fabricante",edifab.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    /*
    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edipro.setText(jsonObject.getString("producto"));
                        edipre.setText(jsonObject.getString("precio"));
                        edifab.setText(jsonObject.getString("fabricante"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    */

    //post solo se optiene el texto json y se convierte a objeto_java
    private void buscarProducto1 (String URL){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getApplicationContext(), "Resultado SQL"+response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonjObject = new JSONObject(response);
                        edipro.setText(jsonjObject.getString("producto"));
                        edipre.setText(jsonjObject.getString("precio"));
                        edifab.setText(jsonjObject.getString("fabricante"));
                        Toast.makeText(getApplicationContext(), "Consulta realizada", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("codigo", edicod.getText().toString());
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    private void eliminarProducto(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Resultado SQL"+response, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                limpiarEdit();
               /* try {
                    //JSONObject jsonjObject = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(), jsonjObject.getString("mensql"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Producto Eliminado", Toast.LENGTH_SHORT).show();
                    //limpiarEdit();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codigo",edicod.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void limpiarEdit(){
        edicod.setText("");
        edipro.setText("");
        edipre.setText("");
        edifab.setText("");
    }

}
