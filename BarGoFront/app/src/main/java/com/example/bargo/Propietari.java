package com.example.bargo;

public class Propietari extends User{

    private static Propietari instance = null;

    public Propietari() {
    }

    public void setPropietariNull(){
        instance = null;
    }

    public void setAll(long id, String nomUsuari, String contrasenya, String token, byte[] imatge) {
        super.setAll(id,nomUsuari,contrasenya,token,imatge);
    }

    public static Propietari getInstance(){
        if(instance == null)
            instance = new Propietari();
        return instance;
    }

}
