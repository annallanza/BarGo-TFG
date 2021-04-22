package com.example.bargo.UsuariConsumidor.Model;


public class EstablimentInfo {
    private Long id;
    private String nom;
    private byte[] imatge;
    private String direccio;
    private String horari;
    private String descripcio;
    private String paginaWeb;
    private Boolean exterior;
    private String ocupacioInterior;
    private String ocupacioExterior;

    private static EstablimentInfo instance = null;

    private EstablimentInfo() {
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

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
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

    public Boolean getExterior() {
        return exterior;
    }

    public void setExterior(Boolean exterior) {
        this.exterior = exterior;
    }

    public String getOcupacioInterior() {
        return ocupacioInterior;
    }

    public void setOcupacioInterior(String ocupacioInterior) {
        this.ocupacioInterior = ocupacioInterior;
    }

    public String getOcupacioExterior() {
        return ocupacioExterior;
    }

    public void setOcupacioExterior(String ocupacioExterior) {
        this.ocupacioExterior = ocupacioExterior;
    }

    public static EstablimentInfo getInstance(){
        if(instance == null)
            instance = new EstablimentInfo();
        return instance;
    }
}
