package com.example.animereminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animereminder.controllers.AnimeController;
import com.example.animereminder.model.Anime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class EditarActivity extends AppCompatActivity {

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

    private Button mBotonEditar;

    private String idEditar;

    private static final int PICK_IMAGE=1;
    Uri imageUri;


    String Nombre;
    String Descripcion;
    String fecha;
    String Cantidad;
    String emision;
    String estudio;
    String autor;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_anime);
        this.mNombre = findViewById(R.id.editar_nombre_anime);
        this.mDescripcion = findViewById(R.id.editar_descripcion);
        mDisplayDate = findViewById(R.id.editar_fecha);
        this.mCantidad = findViewById(R.id.editar_capitulos);
        mHora = findViewById(R.id.editar_horario);
        this.mEstudio = findViewById(R.id.editar_estudio);
        this.mAutor = findViewById(R.id.editar_autor);
        this.mBotonEditar = findViewById(R.id.btneditar);
        this.imagen = findViewById(R.id.editar_imagen);

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
                    mNombre.setText(anime.getNombre());
                    mDescripcion.setText(anime.getDescripcion());
                    mDisplayDate.setText(anime.getFechaDeEstreno());
                    mCantidad.setText(anime.getNumCapitulos());
                    mHora.setText(anime.getHorarioDeEmision());
                    mEstudio.setText(anime.getEstudioDeAnimacion());
                    mAutor.setText(anime.getAutor());
                    idEditar = anime.getId();
                }
                else{
                    System.out.println("No se obtuvieron los datos");
                }
            }
        });

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Editar Anime</font>"));

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Eliga una imagen"), PICK_IMAGE);
            }
        });

        this.mBotonEditar.setOnClickListener(new View.OnClickListener() {
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
                anime.setId(idEditar);
                anime.setNombre(mNombre.getText().toString());
                anime.setDescripcion(mDescripcion.getText().toString());
                anime.setFechaDeEstreno(mDisplayDate.getText().toString());
                anime.setNumCapitulos(mCantidad.getText().toString());
                anime.setHorarioDeEmision(mHora.getText().toString());
                anime.setEstudioDeAnimacion(mEstudio.getText().toString());
                anime.setAutor(mAutor.getText().toString());
                anime.setBorrado(false);

                try {
                    AnimeController.modificarAnime(anime);
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditarActivity.this,"Error al modificar el Anime",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Calendar cal = Calendar.getInstance();
                int año = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog (EditarActivity.this,
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
                Log.d("EditarActivity", "onDateSet: date: " + dia + "/" + mes + "/" + año);
                String fecha = dia + "/" + mes + "/" + año;
                mDisplayDate.setText(fecha);

            }
        };

        mHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditarActivity.this,
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
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                this.imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

