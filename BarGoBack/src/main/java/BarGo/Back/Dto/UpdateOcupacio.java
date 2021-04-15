package BarGo.Back.Dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateOcupacio {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @NotEmpty(message = "La ocupación interior no puede ser un valor nulo ni vacío")
    private String ocupacioInterior;

    @NotEmpty(message = "La ocupación exterior no puede ser un valor nulo ni vacío")
    private String ocupacioExterior;

    public UpdateOcupacio() {
    }

    public UpdateOcupacio(Long id, String ocupacioInterior, String ocupacioExterior) {
        this.id = id;
        this.ocupacioInterior = ocupacioInterior;
        this.ocupacioExterior = ocupacioExterior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOcupacioInterior() {
        return ocupacioInterior;
    }

    public void setOcupacioInterior(String ocupacioInterior) {
        this.ocupacioInterior = ocupacioInterior;
    }

    public String getOcupacioExterior() {
        return ocupacioExterior;
    }

    public void setOcupacioExterior(String ocupacioExterior) {
        this.ocupacioExterior = ocupacioExterior;
    }
}
