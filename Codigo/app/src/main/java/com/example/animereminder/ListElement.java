package com.example.animereminder;

public class ListElement {
    public String titulo;
    public String description;
    public String id;
    public boolean isChecked;
    public ListElement(String textAux, String description, String id, boolean isChecked) {

        this.titulo = textAux;
        this.description = description;
        this.id = id;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
