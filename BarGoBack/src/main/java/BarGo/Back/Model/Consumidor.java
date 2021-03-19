package BarGo.Back.Model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Consumidor")
@PrimaryKeyJoinColumn(name = "usuariId")
public class Consumidor extends Usuari{

    private int puntuacio;

    public Consumidor(){

    }

    public Consumidor(String nomUsuari, String contrasenya, byte[] imatge, int puntuacio) {
        super(nomUsuari, contrasenya, imatge);
        this.puntuacio = puntuacio;
    }

    public int getPuntuacio() {
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio) {
        this.puntuacio = puntuacio;
    }
}
