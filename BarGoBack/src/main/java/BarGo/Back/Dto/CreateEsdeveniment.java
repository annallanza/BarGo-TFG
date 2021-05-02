package BarGo.Back.Dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateEsdeveniment {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @NotBlank(message = "El nombre no puede ser un valor nulo ni vacío")
    private String nom;

    @NotBlank(message = "El dia no puede ser un valor nulo ni vacío")
    private String dia;

    @NotBlank(message = "La hora no puede ser un valor nulo ni vacío")
    private String hora;

    public CreateEsdeveniment() {
    }

    public CreateEsdeveniment(@NotNull(message = "El id no puede ser un valor nulo") Long id, @NotBlank(message = "El nombre no puede ser un valor nulo ni vacío") String nom, @NotBlank(message = "El dia no puede ser un valor nulo ni vacío") String dia, @NotBlank(message = "La hora no puede ser un valor nulo ni vacío") String hora) {
        this.id = id;
        this.nom = nom;
        this.dia = dia;
        this.hora = hora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
