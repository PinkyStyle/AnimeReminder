package com.example.animereminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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



    }

}

