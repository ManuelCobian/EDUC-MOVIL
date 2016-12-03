package com.example.luis.educmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class VerAnuncios extends AppCompatActivity {
    String Ver;
    TextView contenido,Curso,Anuncio,Bienvenido;
    String id_anun;
    String correo;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_anuncios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contenido=(TextView)findViewById(R.id.txtContent);
        Anuncio=(TextView)findViewById(R.id.TxtTItle);
        Curso=(TextView)findViewById(R.id.TxtCurso);
        id_anun= String.valueOf(getIntent().getExtras().getInt("id"));//recibo el di lo convierto en int
        final Intent cambio=new Intent(this,MainActivity.class);//cambia al incio boton regresar
        Anuncio_content anuncio_content = new Anuncio_content();
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        correo=(prefe.getString("mail",""));

        Ver = anuncio_content.post("http://148.213.20.45:88/push/get_anuncios_cuerpo.php",id_anun,correo);


        try {
            JSONObject o = new JSONObject(Ver);

            JSONArray a = o.getJSONArray("anuncios");
            for (int i = 0; i < a.length(); i++) {
                JSONObject contacto=a.getJSONObject(i);

                String title=contacto.getString("titulo");
                getSupportActionBar().setTitle(Html.fromHtml(new String(title.getBytes("UTF-8"))).toString());
                String curso=contacto.getString("curso");
                String content=contacto.getString("anuncio");

                Curso.append(Html.fromHtml(new String(curso.getBytes("UTF-8"))).toString());
                Anuncio.append(Html.fromHtml(new String(title.getBytes("UTF-8"))).toString());
                contenido.append(Html.fromHtml(new String(content.getBytes("UTF-8"))).toString());//aqui para poner acentos
            }
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(VerAnuncios.this,Login.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
