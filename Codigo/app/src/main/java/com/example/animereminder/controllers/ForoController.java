package com.example.animereminder.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.animereminder.model.Foro;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class ForoController {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void crearForo(String idAnime){
        databaseReference.child("Foro").child(idAnime).setValue(new Foro());
    }

    public void crearMensaje(String idAnime, String mensaje){
        databaseReference.child("Foro").child(idAnime).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Foro foro = task.getResult().getValue(Foro.class);
                    if (foro != null) {
                        foro.getMensajes().add(mensaje);
                        databaseReference.child("Foro").child(idAnime).setValue(foro);
                    } else {
                        foro = new Foro();
                        foro.getMensajes().add(mensaje);
                        databaseReference.child("Foro").child(idAnime).setValue(foro);
                    }
                }
                else{
                    Log.d("ERROR: ", "No se obtuvieron los datos");
                }
            }
        });
        databaseReference.child("Foro").child(idAnime).setValue(new Foro());
    }

}
