package BarGo.Back.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Usuari")
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO es una altre opcio
    private Long id;

    @Column(unique = true, nullable = false)
    private String nomUsuari;

    @Column(nullable = false)
    private String contrasenya;

    //@Basic(fetch = FetchType.EAGER)
    //@Lob
    private byte[] imatge;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuari_rol", joinColumns = @JoinColumn(name = "usuari_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> rols = new HashSet<>();

    public Usuari(){

    }

    public Usuari(String nomUsuari, String contrasenya, byte[] imatge) {
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.imatge = imatge;
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

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public Set<Rol> getRols() {
        return rols;
    }

    public void setRols(Set<Rol> rols) {
        this.rols = rols;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }
}
