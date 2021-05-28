package com.example.bargo.UsuariConsumidor.Model;

import java.util.ArrayList;
import java.util.List;

public class RepteListInfo {

    private List<Long> ids;
    private List<String> noms;
    private List<Integer> puntuacions;
    private List<Boolean> complets;
    private List<String> progresos;

    private static RepteListInfo instance = null;

    private RepteListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.puntuacions = new ArrayList<>();
        this.complets = new ArrayList<>();
        this.progresos = new ArrayList<>();
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

    public List<Boolean> getComplets() {
        return complets;
    }

    public void setComplets(List<Boolean> complets) {
        this.complets = complets;
    }

    public List<String> getProgresos() {
        return progresos;
    }

    public void setProgresos(List<String> progresos) {
        this.progresos = progresos;
    }

    public void addRepte(long id, String nom, int puntuacio, boolean complet, String progres){
        this.ids.add(id);
        this.noms.add(nom);
        this.puntuacions.add(puntuacio);
        this.complets.add(complet);
        this.progresos.add(progres);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.puntuacions.clear();
        this.complets.clear();
        this.progresos.clear();
    }

    public static RepteListInfo getInstance(){
        if(instance == null)
            instance = new RepteListInfo();
        return instance;
    }
}
