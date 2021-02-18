package com.example.bargo;

public class BarListInfo {
    private int[] images;
    private String[] bars;
    private String[] adresses;
    private Boolean[] visited;

    public BarListInfo() {
        this.images = new int[]{R.drawable.bar1, R.drawable.bar2, R.drawable.bar3, R.drawable.bar4};
        this.bars = new String[]{"Bar Casa Pepe", "Bar Konbe", "Bar Sirena Verde", "Bar Burot"};
        this.adresses = new String[]{"Plaça de la Bonanova, 4", "Carrer de Déu i Mata, 136", "Carrer de la Indústria, 16", "Carrer de Verdi, 242"};;
        this.visited = new Boolean[]{false, true, false, false};
    }

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    public String[] getBars() {
        return bars;
    }

    public void setBars(String[] bars) {
        this.bars = bars;
    }

    public String[] getAdresses() {
        return adresses;
    }

    public void setAdresses(String[] adresses) {
        this.adresses = adresses;
    }

    public Boolean[] getVisited() {
        return visited;
    }

    public void setVisited(Boolean[] visited) {
        this.visited = visited;
    }
}

