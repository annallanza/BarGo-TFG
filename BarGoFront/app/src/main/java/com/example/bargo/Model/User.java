package com.example.bargo.Model;

public class User {

    private long id = 0;
    private String nom = "Nombre usuario";
    private String contrasenya = "contrasenya";
    private String token = "token";
    private int puntuacio = 0;
    private int codesIntroduced[];

    private static User instance = null;

    private User(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom(){
        return nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public String getContrasenya(){
        return contrasenya;
    }

    public void setContrasenya(String contrasenya){
        this.contrasenya = contrasenya;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPuntuacio(){
        return puntuacio;
    }

    public void setPuntuacio(int puntuacio){
        this.puntuacio = puntuacio;
    }

    public void addPoints(int points) { this.puntuacio += points;}

    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }
}
