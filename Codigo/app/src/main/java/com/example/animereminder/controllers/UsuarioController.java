package com.example.animereminder.controllers;

import android.net.Uri;

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

    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static void crearUsuario(Usuario usuario, String id){
        databaseReference.child("Usuario").child(id).setValue(usuario);
    }

    public static void agregarAnimeMiLista(String idAnime)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Usuario").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    System.out.println(task.getResult().getValue());
                    Usuario usuario = task.getResult().getValue(Usuario.class);
                    usuario.getListaAnime().add(idAnime);
                    databaseReference.child("Usuario").child(user.getUid()).setValue(usuario);
                }
                else{
                    System.out.println("No se obtuvieron los datos");
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
                    System.out.println("No se obtuvieron los datos");
                }
            }
        });

    }
}
