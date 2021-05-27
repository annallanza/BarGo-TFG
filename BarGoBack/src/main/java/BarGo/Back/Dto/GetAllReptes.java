package BarGo.Back.Dto;

public class GetAllReptes {

    private long id;
    private String nom;
    private int puntuacio;
    private boolean complet;
    private String progres;

    public GetAllReptes() {
    }

    public GetAllReptes(long id, String nom, int puntuacio, boolean complet, String progres) {
        this.id = id;
        this.nom = nom;
        this.puntuacio = puntuacio;
        this.complet = complet;
        this.progres = progres;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean isComplet() {
        return complet;
    }

    public void setComplet(boolean complet) {
        this.complet = complet;
    }

    public String getProgres() {
        return progres;
    }

    public void setProgres(String progres) {
        this.progres = progres;
    }
}
