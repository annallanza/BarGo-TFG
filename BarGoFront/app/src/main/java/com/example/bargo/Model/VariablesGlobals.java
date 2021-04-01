package com.example.bargo.Model;

public class VariablesGlobals {

    //public static String urlAPI = "http://192.168.1.157:8080/";
    public static String urlAPI = "http://192.168.43.88:8080/";

    public VariablesGlobals() {
    }

    public static String getUrlAPI() {
        return urlAPI;
    }

    public static void setUrlAPI(String urlAPI) {
        VariablesGlobals.urlAPI = urlAPI;
    }
}
