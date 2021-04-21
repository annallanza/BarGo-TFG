package com.example.bargo.UsuariConsumidor.Model;

import java.util.ArrayList;
import java.util.List;

public class EstablimentListInfo {
    private List<Long> ids;
    private List<String> noms;
    private List<String> imatges;
    private List<String> direccions;
    private List<Boolean> visitats;

    private static EstablimentListInfo instance = null;

    private EstablimentListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.imatges = new ArrayList<>();
        this.direccions = new ArrayList<>();
        this.visitats = new ArrayList<>();
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<String> getNoms() {
        return noms;
    }

    public void setNoms(List<String> noms) {
        this.noms = noms;
    }

    public List<String> getImatges() {
        return imatges;
    }

    public void setImatges(List<String> imatges) {
        this.imatges = imatges;
    }

    public List<String> getDireccions() {
        return direccions;
    }

    public void setDireccions(List<String> direccions) {
        this.direccions = direccions;
    }

    public List<Boolean> getVisitats() {
        return visitats;
    }

    public void setVisitats(List<Boolean> visitats) {
        this.visitats = visitats;
    }

    public void addEstabliment(long id, String nom, String imatge, String direccio, boolean visitat){
        this.ids.add(id);
        this.noms.add(nom);
        this.imatges.add(imatge);
        this.direccions.add(direccio);
        this.visitats.add(visitat);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.imatges.clear();
        this.direccions.clear();
        this.visitats.clear();
    }

    public static EstablimentListInfo getInstance(){
        if(instance == null)
            instance = new EstablimentListInfo();
        return instance;
    }
}

