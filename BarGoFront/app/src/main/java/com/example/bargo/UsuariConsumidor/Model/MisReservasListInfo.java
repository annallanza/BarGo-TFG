package com.example.bargo.UsuariConsumidor.Model;

import java.util.ArrayList;

public class MisReservasListInfo {

    private ArrayList<Long> ids;
    private ArrayList<String> noms;
    private ArrayList<String> direccions;
    private ArrayList<String> informacions;
    private ArrayList<String> imatges;

    private static MisReservasListInfo instance = null;

    public MisReservasListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.direccions = new ArrayList<>();
        this.informacions = new ArrayList<>();
        this.imatges = new ArrayList<>();
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }

    public ArrayList<String> getNoms() {
        return noms;
    }

    public void setNoms(ArrayList<String> noms) {
        this.noms = noms;
    }

    public ArrayList<String> getDireccions() {
        return direccions;
    }

    public void setDireccions(ArrayList<String> direccions) {
        this.direccions = direccions;
    }

    public ArrayList<String> getInformacions() {
        return informacions;
    }

    public void setInformacions(ArrayList<String> informacions) {
        this.informacions = informacions;
    }

    public ArrayList<String> getImatges() {
        return imatges;
    }

    public void setImatges(ArrayList<String> imatges) {
        this.imatges = imatges;
    }

    public void addReserva(long id, String nom, String direccio, String informacio, String imatge){
        this.ids.add(id);
        this.noms.add(nom);
        this.direccions.add(direccio);
        this.informacions.add(informacio);
        this.imatges.add(imatge);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.direccions.clear();
        this.informacions.clear();
        this.imatges.clear();
    }

    public static MisReservasListInfo getInstance(){
        if(instance == null)
            instance = new MisReservasListInfo();
        return instance;
    }
}