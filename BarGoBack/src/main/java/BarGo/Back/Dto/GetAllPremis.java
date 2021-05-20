package BarGo.Back.Dto;

public class GetAllPremis {

    private long id;
    private String nom;
    private int puntuacio;

    public GetAllPremis() {
    }

    public GetAllPremis(long id, String nom, int puntuacio) {
        this.id = id;
        this.nom = nom;
        this.puntuacio = puntuacio;
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
}
