package BarGo.Back.Model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Propietari")
@PrimaryKeyJoinColumn(name = "usuariId")
public class Propietari extends Usuari{

    public Propietari() {
    }

    public Propietari(String nomUsuari, String contrasenya, byte[] imatge) {
        super(nomUsuari, contrasenya, imatge);
    }
}
