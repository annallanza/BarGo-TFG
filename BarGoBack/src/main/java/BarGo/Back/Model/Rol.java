package BarGo.Back.Model;

import BarGo.Back.Enums.NomRol;

import javax.persistence.*;

@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NomRol nomRol;

    public Rol() {
    }

    public Rol(NomRol nomRol) {
        this.nomRol = nomRol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomRol getNomRol() {
        return nomRol;
    }

    public void setNomRol(NomRol nomRol) {
        this.nomRol = nomRol;
    }
}
