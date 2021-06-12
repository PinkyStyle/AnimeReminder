package com.example.animereminder.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.animereminder.MainActivity;
import com.example.animereminder.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Notificacion extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String form = remoteMessage.getFrom();
        if (remoteMessage.getData().size() > 0){
            String titulo = remoteMessage.getData().get("titulo");
            String descripcion = remoteMessage.getData().get("descripcion");
            androidMayorOreo(titulo,descripcion);
        }
    }

    private void androidMayorOreo(String titulo, String descripcion) {
        String id = "mensaje";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        builder.setAutoCancel(true).setWhen(System.currentTimeMillis()).setContentTitle(titulo).setSmallIcon(R.mipmap.ic_launcher).setContentText(descripcion).setContentIntent(clickNotify()).setContentInfo("nuevo");
        Random random = new Random();
        int idNotify = random.nextInt(8000);
        assert notificationManager != null;
        notificationManager.notify(idNotify,builder.build());
    }

    public PendingIntent clickNotify(){
        Intent nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf,0);
    }
}
