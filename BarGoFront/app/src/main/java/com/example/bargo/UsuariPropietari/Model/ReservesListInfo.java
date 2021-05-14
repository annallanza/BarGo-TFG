package com.example.bargo.UsuariPropietari.Model;

import java.util.ArrayList;

public class ReservesListInfo {

    private ArrayList<Long> ids;
    private ArrayList<String> noms;
    private ArrayList<String> informacions;

    private static ReservesListInfo instance = null;

    public ReservesListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.informacions = new ArrayList<>();
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

    public ArrayList<String> getInformacions() {
        return informacions;
    }

    public void setInformacions(ArrayList<String> informacions) {
        this.informacions = informacions;
    }

    public void addReserva(long id, String nom, String informacio){
        this.ids.add(id);
        this.noms.add(nom);
        this.informacions.add(informacio);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.informacions.clear();
    }

    public static ReservesListInfo getInstance(){
        if(instance == null)
            instance = new ReservesListInfo();
        return instance;
    }
}
