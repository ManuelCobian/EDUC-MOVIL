package com.example.luis.educmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Anuncios extends Fragment {

    private GridView mGridView;
    private ProgressBar mProgressBar;
    String id_anun;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://148.213.20.45:88/push/get_anuncios.php?email=lcobian_@ucol.mx";
    private String FEED_URLs = "http://148.213.20.45:88/push_/get_anuncio.php";
    public TextView txt;
    String Corres ,PASSe;
    public Anuncios() {
        // Required empty public constructor

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.anuncios, container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
        txt=(TextView) v.findViewById(R.id.grid_item_course);


        //RECIBO LOS PARAMETROS
        SharedPreferences prefe= getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        Corres=(prefe.getString("mail",""));
        PASSe=(prefe.getString("pass",""));

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                final Intent intenta = new Intent(getActivity(),VerAnuncios.class);

                //Start details activity

                intenta.putExtra("id",v.getId());//esta funcion jala los id
                startActivity(intenta);
                mGridData.get(position);


            }
        });

        //Start download
        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);
        return v;
    }
    //Downloading data asynchronously

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(FEED_URLs);
                List<NameValuePair> parametros = new ArrayList<NameValuePair>();

                parametros.add(new BasicNameValuePair("email",Corres));
                httppost.setEntity(new UrlEncodedFormEntity(parametros));

                HttpResponse httpResponse = httpclient.execute(httppost);

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {

            }

            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI

            if (result == 1) {
                mGridAdapter.setGridData(mGridData);

            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("anuncios");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("titulo");

                id_anun = post.optString("id_anuncio");
                String curso = post.optString("curso");

                item = new GridItem();
                item.setTitle(title);
                item.setCourse(curso);
                item.setId(id_anun);

                mGridData.add(item);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}