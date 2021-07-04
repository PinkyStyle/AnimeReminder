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
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.controllers.ForoController;
import com.example.animereminder.model.Anime;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class AgregarActivity extends AppCompatActivity {

    private EditText mNombre;
    private EditText mDescripcion;
    private EditText mDisplayDate;
    private EditText mCantidad;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText mHora;
    int hora, minuto;
    private EditText mEstudio;
    private EditText mAutor;
    private ImageView imagen;
    private Button mBotonAgregar;

    private AnimeController animeController;
    private static final int PICK_IMAGE=1;
    Uri imageUri;


    String Nombre;
    String Descripcion;
    String fecha;
    String Cantidad;
    String emision;
    String estudio;
    String autor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_anime);
        this.animeController = new AnimeController();

        this.mNombre = findViewById(R.id.nombre_anime);
        this.mDescripcion = findViewById(R.id.descripcion_anime);
        mDisplayDate = findViewById(R.id.fechaDeEstreno);
        this.mCantidad = findViewById(R.id.cantidad_Capitulos);
        mHora = findViewById(R.id.horario_emision);
        this.mEstudio = findViewById(R.id.estudio_animacion);
        this.mAutor = findViewById(R.id.autor);
        this.mBotonAgregar = findViewById(R.id.btnagregar);
        this.imagen = findViewById(R.id.Imagen);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle("Agregar Anime");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseStorage storage = FirebaseStorage.getInstance();



        this.mBotonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Nombre = mNombre.getText().toString();
                Descripcion = mDescripcion.getText().toString();
                fecha = mDisplayDate.getText().toString();
                Cantidad = mCantidad.getText().toString();
                emision = mHora.getText().toString();
                estudio = mEstudio.getText().toString();
                autor = mAutor.getText().toString();
                int cantErrores = 0;

                if (Nombre.isEmpty()) {
                    mNombre.setError("Debe ingresar un título para el anime");
                    cantErrores++;
                }
                if (Descripcion.isEmpty()) {
                    mDescripcion.setError("Debe ingresar una descripción para el anime");
                    cantErrores++;
                }
                if (fecha.isEmpty()) {
                    mDisplayDate.setError("Debe escoger una fecha");
                    cantErrores++;
                }
                if (Cantidad.isEmpty() || Cantidad.equals(null)) {
                    mCantidad.setError("Debe ingresar un número");
                    cantErrores++;
                }
                else if (Integer.valueOf(mCantidad.getText().toString())<=0 || Integer.valueOf(mCantidad.getText().toString())==null) {
                    mCantidad.setError("El número de capítulos no puede ser 0");
                    cantErrores++;
                }
                if (emision.isEmpty()) {
                    mHora.setError("Debe escoger una hora");
                    cantErrores++;
                }
                if (estudio.isEmpty()) {
                    mEstudio.setError("Debe ingresar un estudio para el anime");
                    cantErrores++;
                }
                if (autor.isEmpty()) {
                    mAutor.setError("Debe ingresar un autor para el anime");
                    cantErrores++;
                }
                if(cantErrores >0){
                    return;
                }

                Anime anime = new Anime();
                anime.setId(UUID.randomUUID().toString());
                anime.setNombre(mNombre.getText().toString());
                anime.setDescripcion(mDescripcion.getText().toString());
                anime.setFechaDeEstreno(mDisplayDate.getText().toString());
                anime.setNumCapitulos(mCantidad.getText().toString());
                anime.setHorarioDeEmision(mHora.getText().toString());
                anime.setEstudioDeAnimacion(mEstudio.getText().toString());
                anime.setAutor(mAutor.getText().toString());
                anime.setBorrado(false);

                try {
                    animeController.crearAnime(anime);
                    StorageReference storageRef = storage.getReference().child("anime/"+anime.getId());

                    // Get the data from an ImageView as bytes
                    imagen.setDrawingCacheEnabled(true);
                    imagen.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                    ForoController foroController = new ForoController();
                    foroController.crearForo(anime.getId());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AgregarActivity.this,"Error al agregar el Anime",Toast.LENGTH_SHORT).show();
                }
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Eliga una imagen"), PICK_IMAGE);
            }
        });



        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Calendar cal = Calendar.getInstance();
                int año = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog (AgregarActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        año, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                mes = mes +1;
                Log.d("AgregarActivity", "onDateSet: date: " + dia + "/" + mes + "/" + año);
                String fecha = dia + "/" + mes + "/" + año;
                mDisplayDate.setText(fecha);

            }
        };


        mHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AgregarActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hora = hourOfDay;
                                minuto = minute;
                                String tiempo = hora + ":" + minuto;
                                SimpleDateFormat f24hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24hours.parse(tiempo);
                                    SimpleDateFormat f12hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    mHora.setText(f12hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false

                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hora, minuto);
                timePickerDialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                imageUri = data.getData();
                //   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //   this.imagen.setImageBitmap(bitmap);
                Glide.with(AgregarActivity.this).load(imageUri).into(imagen);
            }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }



}

