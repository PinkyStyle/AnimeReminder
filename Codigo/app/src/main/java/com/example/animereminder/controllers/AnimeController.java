package com.example.animereminder.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animereminder.AgregarActivity;
import com.example.animereminder.ListAdapter;
import com.example.animereminder.ListElement;
import com.example.animereminder.R;
import com.example.animereminder.RegisterActivity;
import com.example.animereminder.TempAdminFragment;
import com.example.animereminder.model.Anime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AnimeController {

    //Variables Firebase
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Button mButtonCrear;
    private Button mButtonModificar;
    private Button mButtonEliminar;

    private List<Anime> listaAnime = new ArrayList<Anime>();
    //ArrayAdapter<Anime> arrayAdapterAnime;

    List<ListElement> elements = new ArrayList<>();

    public void listarAnime(View vista) {
        databaseReference.child("Anime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //para la pertistencia de datos esto de abajo tiene que morir al parecer
                listaAnime.clear();
                elements.clear();

                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Anime anime = objSnaptshot.getValue(Anime.class);
                    if(!anime.isBorrado()){
                        listaAnime.add(anime);
                        elements.add(new ListElement(anime.getNombre(),anime.getDescripcion(), anime.getId()));
                    }
                    //arrayAdapterAnime = new ArrayAdapter<Anime>(AnimeActivity.this, android.R.layout.simple_list_item_1, listaAnime);
                    //La linea de abajo permite enviar el listado al front (SETEA LA VARIABLE LLENANDOLA CON DATOS)
                    //listaVistaAnime.setAdapter(arrayAdapterAnime);
                }
                ListAdapter listAdapter = new ListAdapter(elements,vista.getContext());
                RecyclerView recyclerView = vista.findViewById(R.id.list_anime_all);
                recyclerView.setAdapter(listAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void crearAnime(Anime anime){
        databaseReference.child("Anime").child(anime.getId()).setValue(anime);
    }

    public static void modificarAnime(Anime anime){
        databaseReference.child("Anime").child(anime.getId()).setValue(anime);
    }

    public static void eliminarAnime(String idAnime){
        databaseReference.child("Anime").child(idAnime).child("borrado").setValue(true);
    }
}
