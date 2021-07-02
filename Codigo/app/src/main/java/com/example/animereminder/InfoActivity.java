package com.example.animereminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.model.Anime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class InfoActivity extends AppCompatActivity{
    private TextView nombre;
    private TextView descripcion;
    private TextView fecha;
    private TextView capitulos;
    private TextView hora;
    private TextView estudio;
    private TextView autor;
    private ImageView imagen;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_anime);
        getSupportActionBar().setTitle("Información Anime");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.nombre = findViewById(R.id.info_título);
        this.descripcion = findViewById(R.id.info_descripcion);
        this.fecha = findViewById(R.id.info_estreno);
        this.capitulos= findViewById(R.id.info_capitulos);
        this.hora = findViewById(R.id.info_horario);
        this.estudio = findViewById(R.id.info_estudio);
        this.autor = findViewById(R.id.info_autor);
        this.imagen = findViewById(R.id.info_imagen);

        Bundle b = getIntent().getExtras();
        String idAnime = "";
        if(b != null){
            idAnime = b.getString("id");
        }

        databaseReference.child("Anime").child(idAnime).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){

                    System.out.println(task.getResult().getValue());
                    Anime anime = task.getResult().getValue(Anime.class);
                    nombre.setText(anime.getNombre());
                    descripcion.setText(anime.getDescripcion());
                    fecha.setText(anime.getFechaDeEstreno());
                    capitulos.setText(anime.getNumCapitulos());
                    hora.setText(anime.getHorarioDeEmision());
                    estudio.setText(anime.getEstudioDeAnimacion());
                    autor.setText(anime.getAutor());
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("anime/"+anime.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(InfoActivity.this).load(uri).into(imagen);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", e.toString());
                        }
                    });

                }
                else{
                    System.out.println("No se obtuvieron los datos");
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
