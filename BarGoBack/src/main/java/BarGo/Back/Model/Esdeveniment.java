package BarGo.Back.Model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Esdeveniment")
public class Esdeveniment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dia;

    @Column(nullable = false)
    private Time hora;

    @ManyToOne
    @JoinColumn(name = "establiment_id", referencedColumnName = "id")
    private Establiment establiment;

    public Esdeveniment() {
    }

    public Esdeveniment(String nom, Date dia, Time hora, Establiment establiment) {
        this.nom = nom;
        this.dia = dia;
        this.hora = hora;
        this.establiment = establiment;
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

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Establiment getEstabliment() {
        return establiment;
    }

    public void setEstabliment(Establiment establiment) {
        this.establiment = establiment;
    }

    @PreRemove
    public void eliminarEsdevenimentDeLlistaEsdevenimentsEstabliment(){
        Set<Esdeveniment> esdeveniments = establiment.getEsdeveniments();
        esdeveniments.remove(this);
        establiment.setEsdeveniments(esdeveniments);

        establiment = null;
    }
}
