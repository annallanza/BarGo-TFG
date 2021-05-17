package BarGo.Back.Model;

import BarGo.Back.Enums.TipusOcupacio;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Establiment")
public class Establiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codi;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String direccio;

    @Column(nullable = false)
    private boolean exterior;

    @Column(nullable = false)
    private int numCadires;

    @Column(nullable = false)
    private int numTaules;

    @Column(nullable = false)
    private String horari;

    @Type(type = "text")
    @Column(nullable = false)
    private String descripcio;

    private String paginaWeb;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipusOcupacio ocupacioInterior;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipusOcupacio ocupacioExterior;

    @OneToOne(mappedBy = "establiment")
    private Propietari propietari;

    @OneToMany(mappedBy = "establiment")
    private Set<Esdeveniment> esdeveniments;

    @OneToMany(mappedBy = "establiment")
    private Set<Reserva> reserves;

    @ManyToMany(mappedBy = "establimentsVisitats")
    private Set<Consumidor> consumidorsVisitants;

    public Establiment() {
    }

    public Establiment(String codi, String nom, String direccio, boolean exterior, int numCadires, int numTaules, String horari, String descripcio, String paginaWeb, TipusOcupacio ocupacioInterior, TipusOcupacio ocupacioExterior) {
        this.codi = codi;
        this.nom = nom;
        this.direccio = direccio;
        this.exterior = exterior;
        this.numCadires = numCadires;
        this.numTaules = numTaules;
        this.horari = horari;
        this.descripcio = descripcio;
        this.paginaWeb = paginaWeb;
        this.ocupacioInterior = ocupacioInterior;
        this.ocupacioExterior = ocupacioExterior;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }

    public int getNumCadires() {
        return numCadires;
    }

    public void setNumCadires(int numCadires) {
        this.numCadires = numCadires;
    }

    public int getNumTaules() {
        return numTaules;
    }

    public void setNumTaules(int numTaules) {
        this.numTaules = numTaules;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public TipusOcupacio getOcupacioInterior() {
        return ocupacioInterior;
    }

    public void setOcupacioInterior(TipusOcupacio ocupacioInterior) {
        this.ocupacioInterior = ocupacioInterior;
    }

    public TipusOcupacio getOcupacioExterior() {
        return ocupacioExterior;
    }

    public void setOcupacioExterior(TipusOcupacio ocupacioExterior) {
        this.ocupacioExterior = ocupacioExterior;
    }

    public Propietari getPropietari() {
        return propietari;
    }

    public void setPropietari(Propietari propietari) {
        this.propietari = propietari;
    }

    public Set<Esdeveniment> getEsdeveniments() {
        return esdeveniments;
    }

    public void setEsdeveniments(Set<Esdeveniment> esdeveniments) {
        this.esdeveniments = esdeveniments;
    }

    public Set<Reserva> getReserves() {
        return reserves;
    }

    public void setReserves(Set<Reserva> reserves) {
        this.reserves = reserves;
    }

    public Set<Consumidor> getConsumidorsVisitants() {
        return consumidorsVisitants;
    }

    public void setConsumidorsVisitants(Set<Consumidor> consumidorsVisitants) {
        this.consumidorsVisitants = consumidorsVisitants;
    }

    @PreRemove
    public void preRemove(){
        eliminarConsumidorsVisitats();
        eliminarEsdeveniments();
        eliminarReserves();
    }

    public void eliminarConsumidorsVisitats(){
        for(Consumidor consumidor : this.consumidorsVisitants){
            Set<Establiment> establimentsVisitats = consumidor.getEstablimentsVisitats();
            establimentsVisitats.remove(this);
            consumidor.setEstablimentsVisitats(establimentsVisitats);
        }

        this.consumidorsVisitants.clear();
    }

    public void eliminarEsdeveniments(){

        for(Esdeveniment esdeveniment : esdeveniments){
            esdeveniment.setEstabliment(null);
        }

        this.esdeveniments.clear();
    }

    public void eliminarReserves(){

        for(Reserva reserva : reserves){
            reserva.setEstabliment(null);
        }

        this.reserves.clear();
    }
}
