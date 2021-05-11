package BarGo.Back.Dto;

public class GetAllReservesConsumidor {

    private long id;
    private String nomEstabliment;
    private String imatge;
    private String direccio;
    private String dia;
    private String hora;
    private int numPersones;
    private boolean exterior;

    public GetAllReservesConsumidor() {
    }

    public GetAllReservesConsumidor(long id, String nomEstabliment, String imatge, String direccio, String dia, String hora, int numPersones, boolean exterior) {
        this.id = id;
        this.nomEstabliment = nomEstabliment;
        this.imatge = imatge;
        this.direccio = direccio;
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

    public String getNomEstabliment() {
        return nomEstabliment;
    }

    public void setNomEstabliment(String nomEstabliment) {
        this.nomEstabliment = nomEstabliment;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
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
