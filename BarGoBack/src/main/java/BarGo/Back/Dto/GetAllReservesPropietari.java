package BarGo.Back.Dto;

public class GetAllReservesPropietari {

    private long id;
    private String nomUsuari;
    private String dia;
    private String hora;
    private int numPersones;
    private boolean exterior;

    public GetAllReservesPropietari() {
    }

    public GetAllReservesPropietari(long id, String nomUsuari, String dia, String hora, int numPersones, boolean exterior) {
        this.id = id;
        this.nomUsuari = nomUsuari;
        this.dia = dia;
        this.hora = hora;
        this.numPersones = numPersones;
        this.exterior = exterior;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
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
}
