package BarGo.Back.Dto; //Classes on es defineix l'estructura del JSON que es reb de les requests

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class SignupUsuari {
/* EXEMPLE PER A POSTMAN
{
    "nomUsuari": "annaadmin",
    "contrasenya": "123admin",
    "rols": [
        "admin"
    ]
}
*/

    @NotNull
    private String nomUsuari;

    @NotNull
    @Email(message = "El correo electrónico debe tener el formato correcto")
    private String correu;

    @NotNull
    private String contrasenya;

    private Set<String> rols = new HashSet<>();

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

    public Set<String> getRols() {
        return rols;
    }

    public void setRols(Set<String> rols) {
        this.rols = rols;
    }
}
