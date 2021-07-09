package com.example.animereminder;

public class ComentarioElement {
    public String nombre;
    public String texto;

    public ComentarioElement(String nombre, String texto) {
        this.nombre = nombre;
        this.texto = texto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
