package BarGo.Back.Dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CanviarContrasenya {

    @NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío")
    @Email(message = "El correo electrónico debe tener el formato correcto")
    private String correu;

    public CanviarContrasenya() {
    }

    public CanviarContrasenya(@NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío") @Email(message = "El correo electrónico debe tener el formato correcto") String correu) {
        this.correu = correu;
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }
}
