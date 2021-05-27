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

    @ManyToMany
    @JoinTable(name = "consumidor_premi", joinColumns = @JoinColumn(name = "consumidor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "premi_id", referencedColumnName = "id"))
    private Set<Premi> premisIntercanviats;

    @ManyToMany
    @JoinTable(name = "consumidor_repte", joinColumns = @JoinColumn(name = "consumidor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "repte_id", referencedColumnName = "id"))
    private Set<Repte> reptesRealitzats;

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

    public Set<Premi> getPremisIntercanviats() {
        return premisIntercanviats;
    }

    public void setPremisIntercanviats(Set<Premi> premisIntercanviats) {
        this.premisIntercanviats = premisIntercanviats;
    }

    public Set<Repte> getReptesRealitzats() {
        return reptesRealitzats;
    }

    public void setReptesRealitzats(Set<Repte> reptesRealitzats) {
        this.reptesRealitzats = reptesRealitzats;
    }

    @PreRemove
    public void preRemove(){
        eliminarProductesBescanviats();
        eliminarEstablimentsVisitats();
        eliminarReserves();
        eliminarPremisIntercanviats();
        eliminarReptesRealitzats();
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

    public void eliminarPremisIntercanviats(){
        for(Premi premi : this.premisIntercanviats){
            Set<Consumidor> consumidorsPosseidors = premi.getConsumidorsPosseidors();
            consumidorsPosseidors.remove(this);
            premi.setConsumidorsPosseidors(consumidorsPosseidors);
        }

        this.premisIntercanviats.clear();
    }

    public void eliminarReptesRealitzats(){
        for(Repte repte : this.reptesRealitzats){
            Set<Consumidor> consumidorsPosseidors = repte.getConsumidorsPosseidors();
            consumidorsPosseidors.remove(this);
            repte.setConsumidorsPosseidors(consumidorsPosseidors);
        }

        this.reptesRealitzats.clear();
    }
}
