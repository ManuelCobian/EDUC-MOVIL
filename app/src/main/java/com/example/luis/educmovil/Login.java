package com.example.luis.educmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import peticiones.Anuncios;

public class Login extends AppCompatActivity implements View.OnClickListener {
private Button boton;
    private EditText name,email;
    private RequestQueue requestQueue;
    private static final String URL = "http://148.213.20.45:88/push/get_login.php";
    private StringRequest request;
    private Session session;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boton=(Button)findViewById(R.id.button);
        name=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);
        FirebaseMessaging.getInstance().subscribeToTopic("test");

        session = new Session(this);

        boton.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);


        if(session.loggedin()){
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

           case R.id.button:
               final String nombre=name.getText().toString();
               final String correo=email.getText().toString();

               //MANDO EL CORREO Y EL PASSS PARA ENVIARSELO A LA CLASE HTTPHEADLER
               //para enviar
               SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor=preferencias.edit();
               editor.putString("mail", name.getText().toString());
               editor.putString("pass",email.getText().toString());
               editor.commit();

               String bandera="false";

               if(nombre.length()==0  ){
                   bandera="false";


               }
           if (correo.length()==0)
           {
               bandera="false";

           }
else {
               bandera="verdadero";
           }
               if(bandera=="false"){
                   Toast.makeText(getApplicationContext(),"Para ingresar se requiere que usted escriba su correo electrónico y su contraseña",Toast.LENGTH_SHORT).show();
               }


               if(bandera=="verdadero"){
                   final String token = FirebaseInstanceId.getInstance().getToken();
                   final Intent intent = new Intent(this,MainActivity.class);

                    intent.putExtra("pass",correo);
                   intent.putExtra("DATO",nombre);

                   request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               if(jsonObject.names().get(0).equals("clientes")){

                                   session.setLoggedin(true);




                                   startActivity(intent);

                                   finish();
                               }

                               else {
                                   Toast.makeText(getApplicationContext(), "Los datos que proporcionó no son correctos, por favor verifique y vuelva a intentar" , Toast.LENGTH_SHORT).show();

                                   login="no";
                               }

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }



                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {

                       }
                   }){
                       @Override
                       protected Map<String, String> getParams() throws AuthFailureError {
                           HashMap<String,String> hashMap = new HashMap<String, String>();
                           hashMap.put("email",nombre);
                           hashMap.put("password",correo);
                           hashMap.put("token",token);
                           return hashMap;
                       }
                   };

                   requestQueue.add(request);

                   //aqui traer datos


               }
               break;
        }
    }
}
