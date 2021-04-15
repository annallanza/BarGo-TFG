package BarGo.Back.Dto;

import BarGo.Back.Enums.TipusOcupacio;

public class GetEstablimentById {

    private Long id;
    private String nom;
    private String imatge;
    private String direccio;
    private String horari;
    private String descripcio;
    private String paginaWeb;
    private TipusOcupacio ocupacioInterior;
    private TipusOcupacio ocupacioExterior;

    public GetEstablimentById() {
    }

    public GetEstablimentById(Long id, String nom, String imatge, String direccio, String horari, String descripcio, String paginaWeb, TipusOcupacio ocupacioInterior, TipusOcupacio ocupacioExterior) {
        this.id = id;
        this.nom = nom;
        this.imatge = imatge;
        this.direccio = direccio;
        this.horari = horari;
        this.descripcio = descripcio;
        this.paginaWeb = paginaWeb;
        this.ocupacioInterior = ocupacioInterior;
        this.ocupacioExterior = ocupacioExterior;
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

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public TipusOcupacio getOcupacioInterior() {
        return ocupacioInterior;
    }

    public void setOcupacioInterior(TipusOcupacio ocupacioInterior) {
        this.ocupacioInterior = ocupacioInterior;
    }

    public TipusOcupacio getOcupacioExterior() {
        return ocupacioExterior;
    }

    public void setOcupacioExterior(TipusOcupacio ocupacioExterior) {
        this.ocupacioExterior = ocupacioExterior;
    }
}
