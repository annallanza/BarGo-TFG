package com.example.bargo;

public class Consumidor extends User{

    private int puntuacio = 0;
    private int codesIntroduced[];

    private static Consumidor instance = null;

    private Consumidor() {
    }

    public void setConsumidorNull(){
        instance = null;
    }

    public void setAll(long id, String nomUsuari, String contrasenya, String token, byte[] imatge, int puntuacio) {
        super.setAllUser(id,nomUsuari,contrasenya,token,imatge);
        this.puntuacio = puntuacio;
    }

    public int getPuntuacio(){
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio){
        this.puntuacio = puntuacio;
    }

    public void addPoints(int points) { this.puntuacio += points;}

    public static Consumidor getInstance(){
        if(instance == null)
            instance = new Consumidor();
        return instance;
    }
}
