package com.example.bargo.Model;

public class VariablesGlobals {

    //public static String urlAPI = "http://192.168.1.157:8080/";
    private static String urlAPI = "http://192.168.107.88:8080/";

    private static String secret = "secret";

    public VariablesGlobals() {
    }

    public static String getUrlAPI() {
        return urlAPI;
    }

    public static void setUrlAPI(String urlAPI) {
        VariablesGlobals.urlAPI = urlAPI;
    }

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        VariablesGlobals.secret = secret;
    }
}
