package com.example.bargo;

import java.util.ArrayList;

public class MisReservasInfo {

    private ArrayList<Integer> images;
    private ArrayList<String> bars;
    private ArrayList<String> adresses;
    private ArrayList<String> infoReserva;
    private static MisReservasInfo instance = null;

    public MisReservasInfo() {
        this.images = new ArrayList<Integer>();
        this.images.add(R.drawable.bar1);
        this.images.add(R.drawable.bar2);
        this.images.add(R.drawable.bar3);

        this.bars = new ArrayList<String>();
        this.bars.add("Bar Casa Pepe");
        this.bars.add("Bar Konbe");
        this.bars.add("Bar Sirena Verde");

        this.adresses = new ArrayList<String>();
        this.adresses.add("Plaça de la Bonanova, 4");
        this.adresses.add("Carrer de Déu i Mata, 136");
        this.adresses.add("Carrer de la Indústria, 16");

        this.infoReserva = new ArrayList<String>();
        this.infoReserva.add("Martes 10 nov a las 12.00h\npara 4 personas en la terraza");
        this.infoReserva.add("Jueves 26 nov a las 12.00h\npara 4 personas en la terraza");
        this.infoReserva.add("Lunes 19 nov a las 17.00h\npara 2 personas en el interior");
    }

    public static MisReservasInfo getInstance(){
        if(instance == null)
            instance = new MisReservasInfo();
        return instance;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }

    public ArrayList<String> getBars() {
        return bars;
    }

    public void setBars(ArrayList<String> bars) {
        this.bars = bars;
    }

    public ArrayList<String> getAdresses() {
        return adresses;
    }

    public void setAdresses(ArrayList<String> adresses) {
        this.adresses = adresses;
    }

    public ArrayList<String> getInfoReserva() {
        return infoReserva;
    }

    public void setInfoReserva(ArrayList<String> infoReserva) {
        this.infoReserva = infoReserva;
    }

    public void addReserva(String dia, String hora, String pers, String interior){
        this.images.add(R.drawable.bar1);
        this.bars.add("Bar Casa Pepe");
        this.adresses.add("Plaça de la Bonanova, 4");
        this.infoReserva.add(dia+" a las "+hora+"\npara "+pers+" en "+interior);
    }

    public void cancelReserva(int i) {
        this.images.remove(i);
        this.bars.remove(i);
        this.adresses.remove(i);
        this.infoReserva.remove(i);
        //User.getInstance().addPoints(-1500);
    }
}