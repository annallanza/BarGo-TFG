package BarGo.Back.Dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupConsumidor { //TODO: es el mateix que el SignupUsuari

    @NotEmpty(message = "El nombre de usuario no puede ser un valor nulo ni vacío")
    private String nomUsuari;

    @Size(min = 8, message = "La contraseña debe tener un mínimo de 8 caracteres")
    @NotNull(message = "La contraseña no puede ser un valor nulo")
    @NotBlank(message = "La contraseña no puede ser un valor vacío")
    private String contrasenya;

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}
