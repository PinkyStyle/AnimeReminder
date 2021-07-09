package com.example.animereminder.model;

import java.util.ArrayList;
import java.util.List;

public class Foro {
    private List<Mensaje> mensajes;

    public Foro() {
        this.mensajes = new ArrayList<>();
    }


    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
