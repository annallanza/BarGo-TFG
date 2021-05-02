package BarGo.Back.Dto;

import java.sql.Time;
import java.util.Date;

public class GetEsdevenimentById {

    private Long id;
    private String nom;
    private String nomEstabliment;
    private String direccio;
    private String dia;
    private String hora;

    public GetEsdevenimentById() {
    }

    public GetEsdevenimentById(Long id, String nom, String nomEstabliment, String direccio, String dia, String hora) {
        this.id = id;
        this.nom = nom;
        this.nomEstabliment = nomEstabliment;
        this.direccio = direccio;
        this.dia = dia;
        this.hora = hora;
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
}
