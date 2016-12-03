package com.example.luis.educmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Links extends AppCompatActivity {
String ligas;
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        web=(WebView)findViewById(R.id.web) ;

        //RECIBO LOS PARAMETROS
        SharedPreferences prefe=getSharedPreferences("ligas", Context.MODE_PRIVATE);
        ligas=(prefe.getString("liga",""));

        web.loadUrl(ligas);
        finish();

    }
}
