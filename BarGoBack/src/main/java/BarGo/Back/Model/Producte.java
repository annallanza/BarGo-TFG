package BarGo.Back.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Producte")
public class Producte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codi;

    @ManyToMany(mappedBy = "productesBescanviats")
    private Set<Consumidor> consumidorsBescanviats;

    public Producte() {
    }

    public Producte(String codi) {
        this.codi = codi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public Set<Consumidor> getConsumidorsBescanviats() {
        return consumidorsBescanviats;
    }

    public void setConsumidorsBescanviats(Set<Consumidor> consumidorsBescanviats) {
        this.consumidorsBescanviats = consumidorsBescanviats;
    }
}
