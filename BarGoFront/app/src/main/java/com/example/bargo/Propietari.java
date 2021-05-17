package com.example.bargo;

public class Propietari extends User{

    long idEstabliment;
    String codiEstabliment;
    String nomEstabliment;
    String direccioEstabliment;
    Boolean exteriorEstabliment;
    int numCadiresEstabliment;
    int numTaulesEstabliment;
    String horariEstabliment;
    String descripcioEstabliment;
    String paginaWebEstabliment;

    private static Propietari instance = null;

    public Propietari() {
    }

    public void setPropietariNull(){
        instance = null;
    }

    public void setAllEstabliment(long idEstabliment, String codiEstabliment, String nomEstabliment, String direccioEstabliment, Boolean exteriorEstabliment, int numCadiresEstabliment, int numTaulesEstabliment, String horariEstabliment, String descripcioEstabliment, String paginaWebEstabliment) {
        this.idEstabliment = idEstabliment;
        this.codiEstabliment = codiEstabliment;
        this.nomEstabliment = nomEstabliment;
        this.direccioEstabliment = direccioEstabliment;
        this.exteriorEstabliment = exteriorEstabliment;
        this.numCadiresEstabliment = numCadiresEstabliment;
        this.numTaulesEstabliment = numTaulesEstabliment;
        this.horariEstabliment = horariEstabliment;
        this.descripcioEstabliment = descripcioEstabliment;
        this.paginaWebEstabliment = paginaWebEstabliment;
    }

    public long getIdEstabliment() {
        return idEstabliment;
    }

    public void setIdEstabliment(long idEstabliment) {
        this.idEstabliment = idEstabliment;
    }

    public String getCodiEstabliment() {
        return codiEstabliment;
    }

    public void setCodiEstabliment(String codiEstabliment) {
        this.codiEstabliment = codiEstabliment;
    }

    public String getNomEstabliment() {
        return nomEstabliment;
    }

    public void setNomEstabliment(String nomEstabliment) {
        this.nomEstabliment = nomEstabliment;
    }

    public String getDireccioEstabliment() {
        return direccioEstabliment;
    }

    public void setDireccioEstabliment(String direccioEstabliment) {
        this.direccioEstabliment = direccioEstabliment;
    }

    public Boolean getExteriorEstabliment() {
        return exteriorEstabliment;
    }

    public void setExteriorEstabliment(Boolean exteriorEstabliment) {
        this.exteriorEstabliment = exteriorEstabliment;
    }

    public int getNumCadiresEstabliment() {
        return numCadiresEstabliment;
    }

    public void setNumCadiresEstabliment(int numCadiresEstabliment) {
        this.numCadiresEstabliment = numCadiresEstabliment;
    }

    public int getNumTaulesEstabliment() {
        return numTaulesEstabliment;
    }

    public void setNumTaulesEstabliment(int numTaulesEstabliment) {
        this.numTaulesEstabliment = numTaulesEstabliment;
    }

    public String getHorariEstabliment() {
        return horariEstabliment;
    }

    public void setHorariEstabliment(String horariEstabliment) {
        this.horariEstabliment = horariEstabliment;
    }

    public String getDescripcioEstabliment() {
        return descripcioEstabliment;
    }

    public void setDescripcioEstabliment(String descripcioEstabliment) {
        this.descripcioEstabliment = descripcioEstabliment;
    }

    public String getPaginaWebEstabliment() {
        return paginaWebEstabliment;
    }

    public void setPaginaWebEstabliment(String paginaWebEstabliment) {
        this.paginaWebEstabliment = paginaWebEstabliment;
    }

    public static Propietari getInstance(){
        if(instance == null)
            instance = new Propietari();
        return instance;
    }
}
