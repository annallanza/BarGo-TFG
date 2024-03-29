package BarGo.Back.Dto;

import javax.validation.constraints.NotBlank;

public class LoginUsuari {
/* EXEMPLE PER A POSTMAN
{
    "nomUsuari": "annauser",
    "contrasenya": "123user"
}
 */
    @NotBlank(message = "El nombre de usuario no puede ser un valor nulo ni vacío")
    private String nomUsuari;

    @NotBlank(message = "La contraseña no puede ser un valor nulo ni vacío")
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
