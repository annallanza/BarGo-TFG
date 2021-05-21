package com.example.bargo.UsuariConsumidor.Model;

import com.example.bargo.Consumidor;
import com.example.bargo.R;

import java.util.ArrayList;
import java.util.List;

public class PremiListInfo {

    private List<Long> ids;
    private List<String> noms;
    private List<Integer> puntuacions;

    private static PremiListInfo instance = null;

    private PremiListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.puntuacions = new ArrayList<>();
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

    public List<Integer> getPuntuacions() {
        return puntuacions;
    }

    public void setPuntuacions(List<Integer> puntuacions) {
        this.puntuacions = puntuacions;
    }

    public void addPremi(long id, String nom, int puntuacio){
        this.ids.add(id);
        this.noms.add(nom);
        this.puntuacions.add(puntuacio);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.puntuacions.clear();
    }

    public static PremiListInfo getInstance(){
        if(instance == null)
            instance = new PremiListInfo();
        return instance;
    }


}
