package BarGo.Back.Dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class PutConsumidor {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @PositiveOrZero(message = "La puntuación no puede ser un valor negativo")
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
