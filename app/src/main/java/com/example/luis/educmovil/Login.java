package com.example.luis.educmovil;

import android.content.Intent;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
private Button boton;
    private EditText name,email;
private RequestQueue requestQueue;
    private static final String url="192.168.0.6:8080/fcm/control.php";
    private StringRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boton=(Button)findViewById(R.id.button);
        name=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);

        boton.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

           case R.id.button:
                    final String nombre=name.getText().toString();
               final String correo=email.getText().toString();
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
                   Toast.makeText(getApplicationContext(),"Ingrese Datos",Toast.LENGTH_SHORT).show();
               }


               if(bandera=="verdadero"){
                   Intent intent = new Intent(this,ver.class);
                   intent.putExtra("DATO",nombre);
                   startActivity(intent);
               }
               break;
        }
    }
}
