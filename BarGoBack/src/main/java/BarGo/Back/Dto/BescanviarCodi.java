package BarGo.Back.Dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BescanviarCodi {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @NotBlank(message = "El código no puede ser un valor nulo ni vacío")
    @Pattern(regexp = "(^PROD|^EST)(\\w{8})", message = "El código debe tener el formato PRODxxxxxxxx o ESTxxxxxxxx")
    private String codi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }
}
