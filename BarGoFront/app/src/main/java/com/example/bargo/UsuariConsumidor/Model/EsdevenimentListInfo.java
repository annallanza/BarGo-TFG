package com.example.bargo.UsuariConsumidor.Model;

import java.util.ArrayList;
import java.util.List;

public class EsdevenimentListInfo {
    private List<Long> ids;
    private List<String> noms;
    private List<String> nomsEstabliment;

    private static EsdevenimentListInfo instance = null;

    private EsdevenimentListInfo() {
        this.ids = new ArrayList<>();
        this.noms = new ArrayList<>();
        this.nomsEstabliment = new ArrayList<>();
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

    public List<String> getNomsEstabliment() {
        return nomsEstabliment;
    }

    public void setNomsEstabliment(List<String> nomsEstabliment) {
        this.nomsEstabliment = nomsEstabliment;
    }

    public void addEsdeveniment(long id, String nom, String nomEstabliment){
        this.ids.add(id);
        this.noms.add(nom);
        this.nomsEstabliment.add(nomEstabliment);
    }

    public void resetAll(){
        this.ids.clear();
        this.noms.clear();
        this.nomsEstabliment.clear();
    }

    public static EsdevenimentListInfo getInstance(){
        if(instance == null)
            instance = new EsdevenimentListInfo();
        return instance;
    }

}
