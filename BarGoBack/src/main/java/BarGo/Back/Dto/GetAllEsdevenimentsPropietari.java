package BarGo.Back.Dto;

public class GetAllEsdevenimentsPropietari {

    private long id;
    private String nom;
    private String dia;
    private String hora;

    public GetAllEsdevenimentsPropietari() {
    }

    public GetAllEsdevenimentsPropietari(long id, String nom, String dia, String hora) {
        this.id = id;
        this.nom = nom;
        this.dia = dia;
        this.hora = hora;
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

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
