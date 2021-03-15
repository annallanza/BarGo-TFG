package BarGo.Back.Dto;

import BarGo.Back.Model.Rol;

import java.util.HashSet;
import java.util.Set;

public class GetUsuari {

    private Long id;
    private String nomUsuari;
    private Set<Rol> rols = new HashSet<>();

    public GetUsuari() {
    }

    public GetUsuari(Long id, String nomUsuari, Set<Rol> rols) {
        this.id = id;
        this.nomUsuari = nomUsuari;
        this.rols = rols;
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

    public Set<Rol> getRols() {
        return rols;
    }

    public void setRols(Set<Rol> rols) {
        this.rols = rols;
    }
}
