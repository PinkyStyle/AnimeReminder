package com.example.animereminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    private Button guardar;

    String Nombre;
    String Descripcion;
    String fecha;
    int Cantidad;
    String emision;
    String estudio;
    String autor;

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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Editar Anime</font>"));


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

        /*
        this.guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nombre = mNombre.getText().toString();
                Descripcion = mDescripcion.getText().toString();
                fecha = mDisplayDate.getText().toString();
                Cantidad = Integer.valueOf(mCantidad.getText().toString());
                emision = mHora.getText().toString();
                estudio = mEstudio.getText().toString();
                autor = mAutor.getText().toString();

                if (Nombre.isEmpty()) {
                    mNombre.setError("Debe ingresar un título para el anime");
                }
                if (Descripcion.isEmpty()) {
                    mDescripcion.setError("Debe ingresar una descripción para el anime");
                }
                if (fecha.isEmpty()) {
                    mDisplayDate.setError("Debe escoger una fecha");
                }
                if (Cantidad<=0) {
                    mCantidad.setError("La cantidad de capítulos no puede ser 0");
                }
                if (mCantidad.getText().toString().isEmpty()) {
                    mCantidad.setError("Debe ingresar un número");
                }
                if (emision.isEmpty()) {
                    mHora.setError("Debe escoger una hora");
                }
                if (estudio.isEmpty()) {
                    mEstudio.setError("Debe ingresar un estudio para el anime");
                }
                if (autor.isEmpty()) {
                    mAutor.setError("Debe ingresar un autor para el anime");
                }




            }
        });

        */

    }

}

