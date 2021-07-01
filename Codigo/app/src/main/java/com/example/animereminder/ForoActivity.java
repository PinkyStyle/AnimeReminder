package com.example.animereminder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.model.Anime;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ForoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView nombre;
    private TextView descripcion;
    private ImageView imagen;
    private TextView all;
    private ListView comentarios;
    //private TextInputLayout comentario_usuario;

    private FusedLocationProviderClient mFusedLocationClient;

    private static final int PERMISSION_REQUEST_CODE = 1;

    String id;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foro_anime);
        getSupportActionBar().setTitle("Foro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.nombre = findViewById(R.id.titulo_foro);
        this.descripcion = findViewById(R.id.descripcion_foro);
        this.imagen = findViewById(R.id.imagen_anime_foro);
        this.all = findViewById(R.id.all_anime_foro);
        this.comentarios = findViewById(R.id.comentarios_foro);
        //this.comentario_usuario = findViewById(R.id.comentario_usuario);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("anime/"+anime.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ForoActivity.this).load(uri).into(imagen);
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

        this.fetchLocation();




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void fetchLocation() {
        if (ContextCompat.checkSelfPermission(ForoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Obtiene al última ubicación conocida
                            if (location != null) {
                                Double latitud = location.getLatitude();
                                Double longitud = location.getLongitude();


                            }
                        }
                    });
        }

        else {
            ActivityCompat.requestPermissions(ForoActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // Si la peticion se cancela, el arreglo de peticiones es vacio
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permiso autorizado
                }
                else {
                    new AlertDialog.Builder(this)
                            .setTitle("Debe permitir el uso de su ubicación")
                            .setMessage("Para utilizar el foro, debe permitir a AnimeReminder acceder a su ubicación.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ForoActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            })
                            .create()
                            .show();
                }
                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_anime_foro:
                Intent intent = new Intent(ForoActivity.this, InfoActivity.class);
                Bundle a = new Bundle();
                a.putString("id",id);
                intent.putExtras(a);
                startActivity(intent);
                break;
        }
    }
}





