package com.example.luis.educmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Home extends Fragment {
    private TextView Welcome,USER,Email,txtNombreCompleto;
    TextView names;
    String txt,NombreCompleto;
    SharedPreferences sharedPreference;
    private Session session;
    TextView no_cuenta, trabajador,face,twiter;


    private static final String URL = "http://148.213.20.45:88/push/get_login.php";
    String correo, pass;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    DrawerLayout drawer;
    private GoogleApiClient client;
    public Home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.home, container, false);
        Welcome = (TextView) v.findViewById(R.id.TxtBienvenido);//email
        names = (TextView) v.findViewById(R.id.TxtNoCta);
        Email=(TextView)v.findViewById(R.id.txtNombreCompletos);
        USER=(TextView)v.findViewById(R.id.TextNombre);
        face=(TextView)v.findViewById(R.id.textView2) ;
        twiter=(TextView)v.findViewById(R.id.TextView1);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                    case R.id.textView2:
                        final Intent intentando = new Intent(getActivity(),Links.class);

                        //MANDO EL CORREO Y EL PASSS PARA ENVIARSELO A LA CLASE HTTPHEADLER
                        //para enviar
                        SharedPreferences preferencias=getActivity().getSharedPreferences("ligas", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferencias.edit();
                        editor.putString("liga", "https://www.facebook.com/educenlinea/");

                        editor.commit();
                        startActivity(intentando);
                        break;


                }
            }
        });
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.TextView1:
                        final Intent intentar = new Intent(getActivity(),Links.class);

                        //MANDO EL CORREO Y EL PASSS PARA ENVIARSELO A LA CLASE HTTPHEADLER
                        //para enviar
                        SharedPreferences preferencias= getActivity().getSharedPreferences("ligas", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferencias.edit();
                        editor.putString("liga", "https://twitter.com/@educenlinea");

                        editor.commit();
                        startActivity(intentar);
                        break;
                }
            }
        });
        //RECIBO LOS PARAMETROS
        SharedPreferences prefe = getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        correo=(prefe.getString("mail",""));
        pass=(prefe.getString("pass",""));
        USER.setText(correo);
        //LE MANDO LOS PARAMETROS A LA CLASE
        HttpHadler handler = new HttpHadler();

        txt = handler.post("http://148.213.20.45:88/push/get_login.php",correo,pass);
        Log.i("EDUC",txt);
        //aqui traer datos
        try {
            JSONObject o = new JSONObject(txt);

            JSONArray a = o.getJSONArray("clientes");
            for (int i = 0; i < a.length(); i++) {
                JSONObject contacto=a.getJSONObject(i);


                String name=contacto.getString("name");

                String Correo=contacto.getString("correo");

                String CUENTA = contacto.getString("cuenta");
                Welcome.append(Html.fromHtml(new String(contacto.getString("name").getBytes("UTF-8"))).toString());
                names.append(CUENTA);
                Email.append(Correo);
                USER.setText(Html.fromHtml(new String(contacto.getString("name").getBytes("UTF-8"))).toString());


            }
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return v;
    }
}
