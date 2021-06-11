package com.example.animereminder.controllers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.animereminder.EditarActivity;
import com.example.animereminder.model.Anime;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UsuarioController {

    private String idAnime;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static void crearUsuario(Usuario usuario, String id){
        databaseReference.child("Usuario").child(id).setValue(usuario);
    }

    public void agregarAnimeMiLista(String a) {
        idAnime = a;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Usuario").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Usuario usuario = task.getResult().getValue(Usuario.class);
                    if (usuario != null) {
                        usuario.getListaAnime().add(idAnime);
                        databaseReference.child("Usuario").child(user.getUid()).setValue(usuario);
                    } else {
                        Log.d("usuario: ", "no logeado");
                    }
                }
                else{
                    Log.d("ERROR: ", "No se obtuvieron los datos");
                }
            }
        });
    }

    public static void eliminarAnimeMiLista(String idAnime)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Usuario").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    System.out.println(task.getResult().getValue());
                    Usuario usuario = task.getResult().getValue(Usuario.class);
                    if(usuario.getListaAnime().contains(idAnime)){
                        usuario.getListaAnime().remove(idAnime);
                        databaseReference.child("Usuario").child(user.getUid()).setValue(usuario);
                    }


                }
                else{
                    Log.d("ERROR: ", "No se obtuvieron los datos");
                }
            }
        });

    }
}
