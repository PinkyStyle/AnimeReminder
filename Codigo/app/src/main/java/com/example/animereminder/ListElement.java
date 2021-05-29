package com.example.animereminder;

public class ListElement {
    public String titulo;
    public String description;
    public String id;
    public ListElement(String textAux, String description, String id) {

        this.titulo = textAux;
        this.description = description;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
