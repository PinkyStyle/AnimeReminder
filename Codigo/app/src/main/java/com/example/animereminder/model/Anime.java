package com.example.animereminder.model;

public class Anime {
    private String autor;
    private String descripcion;
    private String estudioDeAnimacion;
    private String fechaDeEstreno;
    private String horarioDeEmision;
    private String id;
    private String nombre;
    //private Map<String, String> plataformaDeTransmision;
    //private Map<String, Mensaje> foro;

    public Anime() {
        //id = UUID.randomUUID().toString();
        //plataformaDeTransmision = new HashMap<>();
        //foro = new HashMap<>();
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

    public String getFechaDeEstreno() {
        return fechaDeEstreno;
    }

    public void setFechaDeEstreno(String fechaDeEstreno) {
        this.fechaDeEstreno = fechaDeEstreno;
    }

    /*public Map<String, String> getPlataformaDeTransmision() {
        return plataformaDeTransmision;
    }*/

    /*public void setPlataformaDeTransmision(Map<String, String> plataformaDeTransmision) {
        this.plataformaDeTransmision = plataformaDeTransmision;
    }*/

    public String getHorarioDeEmision() {
        return horarioDeEmision;
    }

    public void setHorarioDeEmision(String horarioDeEmision) {
        this.horarioDeEmision = horarioDeEmision;
    }

    /*public Map<String, Mensaje> getForo() {
        return foro;
    }*/

    /*public void setForo(Map<String, Mensaje> foro) {
        this.foro = foro;
    }*/
}
