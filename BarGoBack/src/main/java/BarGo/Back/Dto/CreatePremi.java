package BarGo.Back.Dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CreatePremi {

    @NotBlank(message = "El nombre del premio no puede ser un valor nulo ni vacío")
    private String nom;

    @Positive(message = "La puntuación no puede ser un valor negativo")
    private int puntuacio;

    public CreatePremi() {
    }

    public CreatePremi(@NotBlank(message = "El nombre del premio no puede ser un valor nulo ni vacío") String nom, @Positive(message = "La puntuación no puede ser un valor negativo") int puntuacio) {
        this.nom = nom;
        this.puntuacio = puntuacio;
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
