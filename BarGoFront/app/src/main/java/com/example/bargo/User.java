package com.example.bargo;

public class User {
    private String name = "Bar Go";
    private String password = "user";
    private String email = "user";
    private int points = 3500;

    private int codesIntroduced[];

    private static User instance = null;
    private User(){
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public int getPoints(){
        return  points;
    }
    public void setPoints(int points){
        this.points = points;
    }
    public void addPoints(int points) { this.points += points;}

    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }
}
