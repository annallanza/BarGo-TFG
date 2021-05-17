package BarGo.Back.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Consumidor")
@PrimaryKeyJoinColumn(name = "usuariId")
public class Consumidor extends Usuari implements Serializable {

    private int puntuacio;

    @ManyToMany
    @JoinTable(name = "consumidor_producte", joinColumns = @JoinColumn(name = "consumidor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "producte_id", referencedColumnName = "id"))
    private Set<Producte> productesBescanviats;

    @ManyToMany
    @JoinTable(name = "consumidor_establiment", joinColumns = @JoinColumn(name = "consumidor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "establiment_id", referencedColumnName = "id"))
    private Set<Establiment> establimentsVisitats;

    @OneToMany(mappedBy = "consumidor")
    private Set<Reserva> reserves;

    public Consumidor(){

    }

    public Consumidor(String nomUsuari, String correu, String contrasenya, byte[] imatge, int puntuacio) {
        super(nomUsuari, correu, contrasenya, imatge);
        this.puntuacio = puntuacio;
    }

    public int getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio) {
        this.puntuacio = puntuacio;
    }

    public Set<Producte> getProductesBescanviats() {
        return productesBescanviats;
    }

    public void setProductesBescanviats(Set<Producte> productesBescanviats) {
        this.productesBescanviats = productesBescanviats;
    }

    public Set<Establiment> getEstablimentsVisitats() {
        return establimentsVisitats;
    }

    public void setEstablimentsVisitats(Set<Establiment> establimentsVisitats) {
        this.establimentsVisitats = establimentsVisitats;
    }

    public Set<Reserva> getReserves() {
        return reserves;
    }

    public void setReserves(Set<Reserva> reserves) {
        this.reserves = reserves;
    }

    @PreRemove
    public void preRemove(){
        eliminarProductesBescanviats();
        eliminarEstablimentsVisitats();
        eliminarReserves();
    }

    public void eliminarProductesBescanviats(){
        for(Producte producte : this.productesBescanviats){
            Set<Consumidor> consumidorsBescanviats = producte.getConsumidorsBescanviats();
            consumidorsBescanviats.remove(this);
            producte.setConsumidorsBescanviats(consumidorsBescanviats);
        }

        this.productesBescanviats.clear();
    }

    public void eliminarEstablimentsVisitats(){
        for(Establiment establiment : this.establimentsVisitats){
            Set<Consumidor> consumidorsVisitants = establiment.getConsumidorsVisitants();
            consumidorsVisitants.remove(this);
            establiment.setConsumidorsVisitants(consumidorsVisitants);
        }

        this.establimentsVisitats.clear();
    }

    public void eliminarReserves(){

        for(Reserva reserva : reserves){
            reserva.setConsumidor(null);
        }

        this.reserves.clear();
    }
}
