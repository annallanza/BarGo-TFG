package BarGo.Back.Dto;

public class PutConsumidor {

    private Long id;
    private int puntuacio;

    public PutConsumidor() {
    }

    public PutConsumidor(Long id, int puntuacio) {
        this.id = id;
        this.puntuacio = puntuacio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio) {
        this.puntuacio = puntuacio;
    }
}
