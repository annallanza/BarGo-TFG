package com.example.bargo.UsuariConsumidor.Model;


public class EsdevenimentInfo {
    private Long id;
    private String nom;
    private String nomEstabliment;
    private String direccio;
    private String dia;
    private String hora;

    private static EsdevenimentInfo instance = null;

    private EsdevenimentInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomEstabliment() {
        return nomEstabliment;
    }

    public void setNomEstabliment(String nomEstabliment) {
        this.nomEstabliment = nomEstabliment;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public static EsdevenimentInfo getInstance(){
        if(instance == null)
            instance = new EsdevenimentInfo();
        return instance;
    }
}
