package BarGo.Back.Dto;

import javax.validation.constraints.NotNull;

public class IntercanviarPremi {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @NotNull(message = "El id del premio no puede ser un valor nulo")
    private Long idPremi;

    public IntercanviarPremi() {
    }

    public IntercanviarPremi(@NotNull(message = "El id no puede ser un valor nulo") Long id, @NotNull(message = "El id del premio no puede ser un valor nulo") Long idPremi) {
        this.id = id;
        this.idPremi = idPremi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPremi() {
        return idPremi;
    }

    public void setIdPremi(Long idPremi) {
        this.idPremi = idPremi;
    }
}
