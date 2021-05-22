package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    List<ListElement> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeform);
        //Log.i("mensaje", "creación home");
        this.init();
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