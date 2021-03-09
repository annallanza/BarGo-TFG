package BarGo.Back.Dto; //Classes on es defineix l'estructura del JSON que es reb de les requests

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class SignupUsuari {

    @NotBlank
    private String nomUsuari;

    @NotBlank
    private String contrasenya;

    private Set<String> rols = new HashSet<>();

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

    public Set<String> getRols() {
        return rols;
    }

    public void setRols(Set<String> rols) {
        this.rols = rols;
    }
}
