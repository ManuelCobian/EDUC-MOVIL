package peticiones;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis.educmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luis on 27/08/2016.
 */
public class Anuncios extends AppCompatActivity {
    RequestQueue requestQueue;
TextView name ,no_cuenta,trabajador;
    String json="http://192.168.0.8/php/get_cliente.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue= Volley.newRequestQueue(getApplicationContext());


        no_cuenta=(TextView)findViewById(R.id.TxtNumero);
        trabajador=(TextView)findViewById(R.id.TxtNoTrab);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray contactos=response.getJSONArray("contactos");
                    for (int i=0;i<contactos.length();i++){

                        JSONObject contacto=contactos.getJSONObject(i);

                        String email=contacto.getString("email");
                        String nombre=contacto.getString("nombre");
                        String cuenta=contacto.getString("cuenta");
                        String trabajador=contacto.getString("trabajador");

                        if(email=="null"){
                            cuenta="";
                        }
                        if (trabajador=="null"){
                            trabajador="";
                        }
                       name.append(nombre);
                       no_cuenta.append(cuenta);
                        Toast.makeText(getApplicationContext(),nombre,
                                Toast.LENGTH_SHORT).show();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
