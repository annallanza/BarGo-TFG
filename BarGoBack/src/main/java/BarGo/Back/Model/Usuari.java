package BarGo.Back.Model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Usuari")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuari implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO es una altre opcio
    private Long id;

    @Column(unique = true, nullable = false)
    private String nomUsuari;

    @Column(unique = true, nullable = false)
    private String correu;

    @Column(nullable = false)
    private String contrasenya;
    
    private byte[] imatge;

    @Column(unique = true)
    private String codi;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuari_rol", joinColumns = @JoinColumn(name = "usuari_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> rols = new HashSet<>();

    public Usuari(){

    }

    public Usuari(String nomUsuari, String correu, String contrasenya, byte[] imatge) {
        this.nomUsuari = nomUsuari;
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.imatge = imatge;
        this.codi = null;
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

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
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

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }
}
