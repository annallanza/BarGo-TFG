package BarGo.Back.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Repte")
public class Repte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private int puntuacio;

    @ManyToMany(mappedBy = "reptesRealitzats")
    private Set<Consumidor> consumidorsPosseidors;

    public Repte() {
    }

    public Repte(String nom, int puntuacio) {
        this.nom = nom;
        this.puntuacio = puntuacio;
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

    public int getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio) {
        this.puntuacio = puntuacio;
    }

    public Set<Consumidor> getConsumidorsPosseidors() {
        return consumidorsPosseidors;
    }

    public void setConsumidorsPosseidors(Set<Consumidor> consumidorsPosseidors) {
        this.consumidorsPosseidors = consumidorsPosseidors;
    }
}
