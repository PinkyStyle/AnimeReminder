package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private TextView Test;
    private String texto;
    List<ListElement> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeform);
        Bundle b = getIntent().getExtras();
        if (b != null){
            this.texto = b.getString("texto");
        }
        this.Test = findViewById(R.id.testeando);
        this.Test.setText(this.texto);
        this.Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AgregarActivity.class);
                startActivity(intent);
            }
        });
        //Log.i("mensaje", "creación home");
        //this.init();
    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new ListElement("hola","este es un ejemplo de descripción"));
        elements.add(new ListElement("hola2","este es un ejemplo de descripción"));
        elements.add(new ListElement("hola3", "este es un ejemplo de descripción"));
        elements.add(new ListElement("hola4", "este es un ejemplo de descripción"));
        elements.add(new ListElement("hola5", "este es un ejemplo de descripción"));
        elements.add(new ListElement("hola6", "este es un ejemplo de descripción"));

        ListAdapter listAdapter = new ListAdapter(elements,this);
        //Log.i("mensaje", "despues de adapter");
        RecyclerView recyclerView = findViewById(R.id.list_anime_all);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Log.i("mensaje", "antes de set adapter");
        recyclerView.setAdapter(listAdapter);
        //Log.i("mensaje", "despues de set adapter");
    }
}