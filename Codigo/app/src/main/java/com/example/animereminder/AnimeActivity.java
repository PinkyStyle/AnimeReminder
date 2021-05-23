package com.example.animereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animereminder.model.Anime;
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

public class AnimeActivity extends AppCompatActivity {

    //Variables Firebase
    DatabaseReference databaseReference;

    private Button mButtonCrear;
    private Button mButtonModificar;
    private Button mButtonEliminar;

    private List<Anime> listaAnime = new ArrayList<Anime>();
    ArrayAdapter<Anime> arrayAdapterAnime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //agregar layout correspondiente
        //setContentView(R.layout.list_item);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        listarAnime();

        mButtonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDACIONES

                //Funcion
                crearAnime();
            }
        });

        mButtonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDACIONES

                //Funcion
                modificarAnime();
            }
        });

        mButtonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDACIONES

                //Funcion
                eliminarAnime();
            }
        });
    }

    private void listarAnime() {
        databaseReference.child("Anime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //para la pertistencia de datos esto de abajo tiene que morir al parecer
                listaAnime.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Anime anime = objSnaptshot.getValue(Anime.class);
                    listaAnime.add(anime);

                    arrayAdapterAnime = new ArrayAdapter<Anime>(AnimeActivity.this, android.R.layout.simple_list_item_1, listaAnime);
                    //La linea de abajo permite enviar el listado al front (SETEA LA VARIABLE LLENANDOLA CON DATOS)
                    //listaVistaAnime.setAdapter(arrayAdapterAnime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void crearAnime(){
        SimpleDateFormat dia = new SimpleDateFormat("dd-mm-yyyy");
        Date date = dia.parse("13:24:40",null);
        SimpleDateFormat tiempo = new SimpleDateFormat("HH:MM:SS");
        Date time = tiempo.parse("13:24:40",null);
        Anime anime = new Anime();
        anime.setId(UUID.randomUUID().toString());
        anime.setNombre("setNombre");
        anime.setDescripcion("setDescripcion");
        anime.setAutor("setAutor");
        anime.setEstudioDeAnimacion("setEstudioDeAnimacion");
        anime.setFechaDeEstreno(date);
        anime.setHorarioDeEmision(time);
        databaseReference.child("Anime").child(anime.getId()).setValue(anime);
    }

    public void modificarAnime(){
        SimpleDateFormat dia = new SimpleDateFormat("dd-mm-yyyy");
        Date date = dia.parse("13:24:40",null);
        SimpleDateFormat tiempo = new SimpleDateFormat("HH:MM:SS");
        Date time = tiempo.parse("13:24:40",null);
        Anime anime = new Anime();
        anime.setId("getIdFront");
        anime.setNombre("setNombre".trim());
        anime.setDescripcion("setDescripcion".trim());
        anime.setAutor("setAutor".trim());
        anime.setEstudioDeAnimacion("setEstudioDeAnimacion".trim());
        anime.setFechaDeEstreno(date);
        anime.setHorarioDeEmision(time);
        databaseReference.child("Anime").child(anime.getId()).setValue(anime);
    }

    public void eliminarAnime(){
        Anime anime = new Anime();
        anime.setId("getIdFront");
        databaseReference.child("Anime").child(anime.getId()).removeValue();
    }
}
