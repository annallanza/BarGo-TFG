package BarGo.Back.Dto;

import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Rol;

import java.util.HashSet;
import java.util.Set;

public class GetPropietari {

    private Long id;
    private String nomUsuari;
    private byte[] imatge;
    private Set<Rol> rols = new HashSet<>();
    private GetEstabliment establiment;

    public GetPropietari() {
    }

    public GetPropietari(Long id, String nomUsuari, byte[] imatge, Set<Rol> rols, GetEstabliment establiment) {
        this.id = id;
        this.nomUsuari = nomUsuari;
        this.imatge = imatge;
        this.rols = rols;
        this.establiment = establiment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }

    public Set<Rol> getRols() {
        return rols;
    }

    public void setRols(Set<Rol> rols) {
        this.rols = rols;
    }

    public GetEstabliment getEstabliment() {
        return establiment;
    }

    public void setEstabliment(GetEstabliment establiment) {
        this.establiment = establiment;
    }
}
