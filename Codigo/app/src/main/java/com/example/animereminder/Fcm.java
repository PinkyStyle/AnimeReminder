package com.example.animereminder;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class Fcm  extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.d("token", s);
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String form = remoteMessage.getFrom();
        Log.d("from", form);
        if (remoteMessage.getNotification() != null){
            Log.d("Titulo",remoteMessage.getNotification().getTitle());
            Log.d("body", remoteMessage.getNotification().getBody());
        }
    }
}
