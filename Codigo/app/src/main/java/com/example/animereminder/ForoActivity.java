package com.example.animereminder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.ForoController;
import com.example.animereminder.model.Anime;
import com.example.animereminder.model.Mensaje;
import com.example.animereminder.model.Usuario;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ForoActivity extends AppCompatActivity  {
    private TextView nombre;
    private TextView descripcion;
    private ImageView imagen;
    private TextView all;
    private RecyclerView comentarios;

    private List<ComentarioElement> elementos;

    private ImageButton enviar;
    private TextInputEditText comentario_usuario;

    private FusedLocationProviderClient mFusedLocationClient;

    private static final int PERMISSION_REQUEST_CODE = 1;

    String admin;
    String idAnime;
    String nombre_usuario;

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
        this.comentario_usuario = findViewById(R.id.comentario_usuario);
        this.enviar = findViewById(R.id.enviar_comentario);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Bundle b = getIntent().getExtras();
        idAnime = "";
        admin = "";
        if(b != null){
            idAnime = b.getString("id");
            admin = b.getString("admin");
        }
        else{
            Toast.makeText(ForoActivity.this, "No se encontró el foro", Toast.LENGTH_SHORT).show();
            finish();
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (admin==null){
            databaseReference.child("Usuario").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Usuario usuario = task.getResult().getValue(Usuario.class);
                        nombre_usuario = usuario.getNickname();
                    }
                }
            });
        }

        this.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForoActivity.this, InfoActivity.class);
                Bundle a = new Bundle();
                a.putString("id",idAnime);
                intent.putExtras(a);
                startActivity(intent);
            }
        });


        this.enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensaje mensaje = new Mensaje();
                if(nombre_usuario!=null){
                    mensaje.setUsuario(nombre_usuario);
                }
                else{
                    mensaje.setUsuario("ADMIN");
                }


                mensaje.setMensaje(comentario_usuario.getText().toString());
                ForoController foroController = new ForoController();
                foroController.crearMensaje(idAnime, mensaje);
                comentario_usuario.setText("");
            }
        });

        databaseReference.child("Anime").child(idAnime).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Anime anime = task.getResult().getValue(Anime.class);
                    nombre.setText(anime.getNombre());
                    descripcion.setText(anime.getDescripcion());
                    String id = anime.getId();
                    DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                    connectedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean connected = snapshot.getValue(Boolean.class);
                            if (connected) {
                                long ONE_MEGABYTE = 1024 * 1024;
                                StorageReference storageRef = storage.getReference();
                                storageRef.child("anime/"+id).getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {

                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                options.inScaled = false;
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                                                imagen.setImageBitmap(bitmap);
                                                ContextWrapper cw = new ContextWrapper(getBaseContext());
                                                File dirImages = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                File myPath = new File(dirImages, id + ".jpg");
                                                FileOutputStream fos = null;
                                                try {
                                                    fos = new FileOutputStream(myPath);
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                    fos.flush();
                                                } catch (FileNotFoundException ex) {
                                                    ex.printStackTrace();
                                                } catch (IOException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } else {

                                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), id + ".jpg");
                                if(file.exists()) {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inScaled = false;
                                    Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                    if (bmp != null) {
                                        imagen.setImageBitmap(bmp);
                                        bmp = null;
                                    }
                                    file = null;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    System.out.println("No se obtuvieron los datos");
                }
            }
        });


        databaseReference.child("Foro").child(idAnime).child("mensajes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<ComentarioElement> elementos = new ArrayList<>();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Mensaje mensaje = objSnaptshot.getValue(Mensaje.class);
                    elementos.add(new ComentarioElement(mensaje.getUsuario(), mensaje.getMensaje()));
                    //comentarios.setAdapter(adapter);
                }
                ComentarioAdapter comentarioAdapter = new ComentarioAdapter(elementos, ForoActivity.this);
                comentarios = findViewById(R.id.comentarios_Foro);
                comentarios.setHasFixedSize(true);
                comentarios.setLayoutManager(new LinearLayoutManager(ForoActivity.this));
                comentarios.setAdapter(comentarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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


    public void init() {

        this.elementos = new ArrayList<>();
        elementos.add(new ComentarioElement("persona1","este es un ejemplo de mensaje1"));
        elementos.add(new ComentarioElement("persona2", "este es un ejemplo de mensaje2"));
        elementos.add(new ComentarioElement("persona3", "este es un ejemplo de mensaje3"));
        elementos.add(new ComentarioElement("persona4", "este es un ejemplo de mensaje4"));
        elementos.add(new ComentarioElement("persona5", "este es un ejemplo de mensaje5"));
        ComentarioAdapter comentarioAdapter = new ComentarioAdapter(elementos, this);
        this.comentarios = findViewById(R.id.comentarios_Foro);
        this.comentarios.setHasFixedSize(true);
        this.comentarios.setLayoutManager(new LinearLayoutManager(this));
        this.comentarios.setAdapter(comentarioAdapter);
    }
}





