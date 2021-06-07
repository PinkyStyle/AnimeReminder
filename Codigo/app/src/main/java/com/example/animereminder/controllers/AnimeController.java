package com.example.animereminder.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animereminder.AgregarActivity;
import com.example.animereminder.ListAdapter;
import com.example.animereminder.ListElement;
import com.example.animereminder.ListUserAdapter;
import com.example.animereminder.ListUserMiListaAdapter;
import com.example.animereminder.R;
import com.example.animereminder.RegisterActivity;
import com.example.animereminder.TempAdminFragment;
import com.example.animereminder.model.Anime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private static List<Anime> listaAnime = new ArrayList<Anime>();
    private static List<String> miLista = new ArrayList<>();
    //ArrayAdapter<Anime> arrayAdapterAnime;

    List<ListElement> elements = new ArrayList<>();


    public void listarAnime(View vista, String opcion) {
        databaseReference.child("Anime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAnime.clear();
                elements.clear();
                AnimeController.getMiLista();

                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Anime anime = objSnaptshot.getValue(Anime.class);
                    if(!anime.isBorrado()){
                        listaAnime.add(anime);
                        boolean checked = false;

                        System.out.println("------------TAMAÑO FUNCION listarAnime: "+ miLista.size());
                        for (String id : miLista){
                            if (anime.getId().equals(id)){
                                checked = true;
                                break;
                            }
                        }
                        elements.add(new ListElement(anime.getNombre(),anime.getDescripcion(), anime.getId(), checked));
                    }
                    //arrayAdapterAnime = new ArrayAdapter<Anime>(AnimeActivity.this, android.R.layout.simple_list_item_1, listaAnime);
                    //La linea de abajo permite enviar el listado al front (SETEA LA VARIABLE LLENANDOLA CON DATOS)
                    //listaVistaAnime.setAdapter(arrayAdapterAnime);
                }
                System.out.println("///////////////////Lista listaAnime: "+listaAnime.size());
                switch (opcion){
                    case "a":
                        ListAdapter listAdapter = new ListAdapter(elements,vista.getContext());
                        RecyclerView recyclerView = vista.findViewById(R.id.list_anime_all);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(vista.getContext()));
                        recyclerView.setAdapter(listAdapter);
                        break;
                    case "b":
                        ListUserAdapter listUserAdapter = new ListUserAdapter(elements,vista.getContext());
                        RecyclerView recyclerView2 = vista.findViewById(R.id.list_anime_all_user);
                        recyclerView2.setHasFixedSize(true);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(vista.getContext()));
                        recyclerView2.setAdapter(listUserAdapter);
                        break;
                    case "c":
                        ListUserMiListaAdapter listUserMiListaAdapter = new ListUserMiListaAdapter(elements, vista.getContext());
                        RecyclerView recyclerView3 = vista.findViewById(R.id.list_anime_milista_user);
                        recyclerView3.setHasFixedSize(true);
                        recyclerView3.setLayoutManager(new LinearLayoutManager(vista.getContext()));
                        recyclerView3.setAdapter(listUserMiListaAdapter);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void listarMiAnime(View vista){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<ListElement> listaElemento = new ArrayList<>();
        databaseReference.child("Usuario").child(user.getUid()).child("listaAnime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaElemento.clear();
                miLista.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    String idAnime = objSnaptshot.getValue().toString();
                    for (Anime anime : listaAnime){
                        if (anime.getId().equals(idAnime)){
                            listaElemento.add(new ListElement(anime.getNombre(), anime.getDescripcion(), anime.getId(), true));
                            miLista.add(anime.getId());
                            break;
                        }
                    }
                }
                ListUserMiListaAdapter listUserMiListaAdapter = new ListUserMiListaAdapter(listaElemento, vista.getContext());
                RecyclerView recyclerView3 = vista.findViewById(R.id.list_anime_milista_user);
                recyclerView3.setHasFixedSize(true);
                recyclerView3.setLayoutManager(new LinearLayoutManager(vista.getContext()));
                recyclerView3.setAdapter(listUserMiListaAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getMiLista(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Usuario").child(user.getUid()).child("listaAnime").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    miLista = (List)task.getResult().getValue();
                    System.out.println("||||||||||||||TAMAÑO FUNCION getMiLista: "+miLista.size());

                }
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
