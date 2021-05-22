package com.example.animereminder;

public class ListElement {
    public String titulo;
    public String description;
    public ListElement(String textAux, String description) {

        this.titulo = textAux;
        this.description = description;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
