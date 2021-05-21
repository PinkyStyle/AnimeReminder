package com.example.animereminder.model;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String nickname;
    private String correo;
    private Boolean baneado;
    private Map<String, Anime> listaAnime;

    public Usuario() {
        listaAnime = new HashMap<>();
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

    public Map<String, Anime> getListaAnime() {
        return listaAnime;
    }

    public void setListaAnime(Map<String, Anime> listaAnime) {
        this.listaAnime = listaAnime;
    }
}
