package com.example.animereminder.controllers;

import com.example.animereminder.model.Anime;
import com.example.animereminder.model.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsuarioController {

    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static void crearUsuario(Usuario usuario, String id){
        databaseReference.child("Usuario").child(id).setValue(usuario);
    }
}
