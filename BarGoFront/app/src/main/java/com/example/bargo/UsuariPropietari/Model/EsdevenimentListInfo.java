package com.example.bargo.UsuariPropietari.Model;

import java.util.ArrayList;
import java.util.List;

public class EsdevenimentListInfo {
    private List<Long> ids;
    private List<String> noms;
    private List<String> dies;
    private List<String> hores;

    private static EsdevenimentListInfo instance = null;

    private EsdevenimentListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.dies = new ArrayList<>();
        this.hores = new ArrayList<>();
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

    public List<String> getDies() {
        return dies;
    }

    public void setDies(List<String> dies) {
        this.dies = dies;
    }

    public List<String> getHores() {
        return hores;
    }

    public void setHores(List<String> hores) {
        this.hores = hores;
    }

    public void addEsdeveniment(long id, String nom, String dia, String hora){
        this.ids.add(id);
        this.noms.add(nom);
        this.dies.add(dia);
        this.hores.add(hora);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.dies.clear();
        this.hores.clear();
    }

    public static EsdevenimentListInfo getInstance(){
        if(instance == null)
            instance = new EsdevenimentListInfo();
        return instance;
    }

}
