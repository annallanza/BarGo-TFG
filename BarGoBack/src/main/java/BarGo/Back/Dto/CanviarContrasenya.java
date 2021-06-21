package BarGo.Back.Dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CanviarContrasenya {

    @NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío")
    @Email(message = "El correo electrónico debe tener el formato correcto")
    private String correu;

    @NotBlank(message = "El código no puede ser un valor nulo ni vacío")
    @Pattern(regexp = "(\\w{8})", message = "El código debe tener el formato xxxxxxxx")
    private String codi;

    @Size(min = 8, message = "La contraseña debe tener un mínimo de 8 caracteres")
    @NotBlank(message = "La contraseña no puede ser un valor nulo ni vacío")
    private String contrasenya;

    public CanviarContrasenya() {
    }

    public CanviarContrasenya(@NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío") @Email(message = "El correo electrónico debe tener el formato correcto") String correu, @NotBlank(message = "El código no puede ser un valor nulo ni vacío") @Pattern(regexp = "(\\w{8})", message = "El código debe tener el formato xxxxxxxx") String codi, @Size(min = 8, message = "La contraseña debe tener un mínimo de 8 caracteres") @NotBlank(message = "La contraseña no puede ser un valor nulo ni vacío") String contrasenya) {
        this.correu = correu;
        this.codi = codi;
        this.contrasenya = contrasenya;
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}
