package com.example.animereminder.model;

import java.util.ArrayList;
import java.util.List;

public class Foro {
    private List<String> mensajes;

    public Foro() {
        this.mensajes = new ArrayList<>();
    }


    public List<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<String> mensajes) {
        this.mensajes = mensajes;
    }
}
