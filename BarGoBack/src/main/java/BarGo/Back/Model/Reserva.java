package BarGo.Back.Model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dia;

    @Column(nullable = false)
    private Time hora;

    @Column(nullable = false)
    private int numPersones;

    @Column(nullable = false)
    private boolean exterior;

    @ManyToOne
    @JoinColumn(name = "establiment_id", referencedColumnName = "id")
    private Establiment establiment;

    @ManyToOne
    @JoinColumn(name = "consumidor_id", referencedColumnName = "usuariId")
    private Consumidor consumidor;

    public Reserva() {
    }

    public Reserva(Date dia, Time hora, int numPersones, boolean exterior, Establiment establiment, Consumidor consumidor) {
        this.dia = dia;
        this.hora = hora;
        this.numPersones = numPersones;
        this.exterior = exterior;
        this.establiment = establiment;
        this.consumidor = consumidor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNumPersones() {
        return numPersones;
    }

    public void setNumPersones(int numPersones) {
        this.numPersones = numPersones;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }

    public Establiment getEstabliment() {
        return establiment;
    }

    public void setEstabliment(Establiment establiment) {
        this.establiment = establiment;
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(Consumidor consumidor) {
        this.consumidor = consumidor;
    }

    @PreRemove
    public void preRemove(){
        eliminarEstabliment();
        eliminarConsumidor();
    }

    public void eliminarEstabliment(){
        if(establiment != null) {
            Set<Reserva> reserves = establiment.getReserves();
            reserves.remove(this);
            establiment.setReserves(reserves);

            establiment = null;
        }
    }

    public void eliminarConsumidor(){
        if(consumidor != null) {
            Set<Reserva> reserves = consumidor.getReserves();
            reserves.remove(this);
            consumidor.setReserves(reserves);

            consumidor = null;
        }
    }
}
