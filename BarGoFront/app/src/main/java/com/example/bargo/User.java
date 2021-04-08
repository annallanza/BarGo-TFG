package com.example.bargo;

public class User {

    private long id = 0;
    private String nom = "Nombre usuario";
    private String contrasenya = "contrasenya";
    private String token = "token";
    private byte[] imatge;


    public User() {

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

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }

    public void setAlmostAll(long id, String nomUsuari, String contrasenya, String token){
        this.setId(id);
        this.setNom(nomUsuari);
        this.setContrasenya(contrasenya);
        this.setToken(token);
    }

    public void setAllUser(long id, String nomUsuari, String contrasenya, String token, byte[] imatge){
        this.setId(id);
        this.setNom(nomUsuari);
        this.setContrasenya(contrasenya);
        this.setToken(token);
        this.setImatge(imatge);
    }
}
