package com.example.animereminder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario {

    private String nickname;
    private String correo;
    private Boolean baneado;
    private List<String> listaAnime;

    public Usuario() {
        listaAnime = new ArrayList<String>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getBaneado() {
        return baneado;
    }

    public void setBaneado(Boolean baneado) {
        this.baneado = baneado;
    }

    public List<String> getListaAnime() {
        return listaAnime;
    }

    public void setListaAnime(List<String> listaAnime) {
        this.listaAnime = listaAnime;
    }
}
