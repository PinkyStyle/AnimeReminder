package com.example.animereminder.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Anime {

    private String id;
    private String nombre;
    private String descripcion;
    private String estudioDeAnimacion;
    private String autor;
    private Date fechaDeEstreno;
    private Map<String, String> plataformaDeTransmision;
    private Date HorarioDeEmision;
    private Map<String, Mensaje> foro;

    public Anime() {
        plataformaDeTransmision = new HashMap<>();
        foro = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstudioDeAnimacion() {
        return estudioDeAnimacion;
    }

    public void setEstudioDeAnimacion(String estudioDeAnimacion) {
        this.estudioDeAnimacion = estudioDeAnimacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getFechaDeEstreno() {
        return fechaDeEstreno;
    }

    public void setFechaDeEstreno(Date fechaDeEstreno) {
        this.fechaDeEstreno = fechaDeEstreno;
    }

    public Map<String, String> getPlataformaDeTransmision() {
        return plataformaDeTransmision;
    }

    public void setPlataformaDeTransmision(Map<String, String> plataformaDeTransmision) {
        this.plataformaDeTransmision = plataformaDeTransmision;
    }

    public Date getHorarioDeEmision() {
        return HorarioDeEmision;
    }

    public void setHorarioDeEmision(Date horarioDeEmision) {
        HorarioDeEmision = horarioDeEmision;
    }

    public Map<String, Mensaje> getForo() {
        return foro;
    }

    public void setForo(Map<String, Mensaje> foro) {
        this.foro = foro;
    }
}
