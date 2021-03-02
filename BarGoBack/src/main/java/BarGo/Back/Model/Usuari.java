package BarGo.Back.Model;

import javax.persistence.*;

@Entity
@Table(name = "Usuari")
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //IDENTITY es una altre opcio
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    @Column(nullable = false)
    private String contrasenya;

    public Usuari(){

    }

    public Usuari(String nom, String contrasenya) {
        this.nom = nom;
        this.contrasenya = contrasenya;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}
