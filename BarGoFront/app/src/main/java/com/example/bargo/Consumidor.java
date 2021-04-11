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
