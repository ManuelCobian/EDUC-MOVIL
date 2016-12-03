package com.example.luis.educmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by luis on 03/07/2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService  {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();


        registerToken(token);
    }


    private void registerToken(String token) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("token",token)


                .build();

        Request request = new Request.Builder()
//http://192.168.0.6:8080/fcm/register.php
                //"http://148.213.20.45:88/push/register.php"
                .url("http://148.213.20.45:88/push/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
