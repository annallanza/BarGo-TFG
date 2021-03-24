package BarGo.Back.Model;

import javax.persistence.*;

@Entity
@Table(name = "Propietari")
@PrimaryKeyJoinColumn(name = "usuariId")
public class Propietari extends Usuari{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "establimentId", referencedColumnName = "id")
    private Establiment establiment;

    public Propietari() {
    }

    public Propietari(String nomUsuari, String contrasenya, byte[] imatge, Establiment establiment) {
        super(nomUsuari, contrasenya, imatge);
        this.establiment = establiment;
    }
}
