package com.example.luis.educmovil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by luis on 03/07/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("idAnuncio"));
    }

    private void showNotification(String message,String idAnuncio) {


        Intent i = new Intent(this,VerAnuncios.class);
        i.putExtra("id",Integer.parseInt(idAnuncio));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT < 20) {
            builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("Educ Movil")
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_educverde)
                    .setContentIntent(pendingIntent)
                    .setNumber(Integer.parseInt(idAnuncio));
        }else{
            builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("Educ Movil")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.educa)
                    .setContentIntent(pendingIntent)
                    .setNumber(Integer.parseInt(idAnuncio));
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());


    }
}
