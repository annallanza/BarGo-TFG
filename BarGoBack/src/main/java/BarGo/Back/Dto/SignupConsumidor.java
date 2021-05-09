package BarGo.Back.Dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupConsumidor { //TODO: es el mateix que el SignupUsuari

    @NotBlank(message = "El nombre de usuario no puede ser un valor nulo ni vacío")
    private String nomUsuari;

    @NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío")
    @Email(message = "El correo electrónico debe tener el formato correcto")
    private String correu;

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

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}
