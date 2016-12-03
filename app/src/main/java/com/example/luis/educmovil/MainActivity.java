package com.example.luis.educmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    boolean fragmentTransaction = false;
    Fragment fragment = null;
    private TextView Welcome,USER,Email,txtNombreCompleto;
    String txt,NombreCompleto;
    private static final String URL = "http://148.213.20.45:88/push/get_login.php";
    String correo, pass;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
        correo=(prefe.getString("mail",""));
        pass=(prefe.getString("pass",""));
        //LE MANDO LOS PARAMETROS A LA CLASE
        HttpHadler handler = new HttpHadler();

        txt = handler.post("http://148.213.20.45:88/push/get_login.php",correo,pass);
        Log.i("EDUC",correo+" "+pass);
        //aqui traer datos
        try {
            JSONObject o = new JSONObject(txt);

            JSONArray a = o.getJSONArray("clientes");
            for (int i = 0; i < a.length(); i++) {
                JSONObject contacto=a.getJSONObject(i);
                NombreCompleto = Html.fromHtml(new String(contacto.getString("name").getBytes("UTF-8"))).toString();
            }
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        View header=navigationView.getHeaderView(0);
        txtNombreCompleto = (TextView) header.findViewById(R.id.txtNombreCompleto);
        txtNombreCompleto.setText(NombreCompleto);
        fragment = new Home();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i("EDUC", String.valueOf(id));
        //noinspection SimplifiableIfStatement
        switch(id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_salir:
                Intent intent = new Intent(this, Login.class);
                session = new Session(this);
                //aqui cierras sesion
                logout();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new Home();
            fragmentTransaction = true;
        }else if (id == R.id.anuncios) {
            fragment = new Anuncios();
            fragmentTransaction = true;
        }  else if (id == R.id.nav_material) {
            fragment = new MaterialStudio();
            fragmentTransaction = true;
        }else if (id == R.id.actividades) {
            fragment = new MaterialStudio();
            fragmentTransaction = true;
        }else if (id == R.id.nav_salir) {
            Intent intent = new Intent(this, Login.class);
            session = new Session(this);
            //aqui cierras sesion
            logout();
        }
        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout(){
        session.setLoggedin(false);
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
