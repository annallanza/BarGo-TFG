package com.example.bargo;

public class EventListInfo {
    public int[] images;
    public String[] bares;
    public String[] events;

    public EventListInfo() {
        setImages();
        setBars();
        setEvents();
    }

    public int[] getImages() {
        return images;
    }

    public void setImages() {
        this.images = new int[]{R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,};
    }

    public String[] getBars() {
        return bares;
    }

    public void setBars() {
        this.bares = new String[] {"Bar Casa Pepe",
                "Bar Sirena Verde",
                "Bar Diagonal",
                "Bar Luna"};
    }


    public String[] getEvents() {
        return events;
    }

    public void setEvents() {
        this.events = new String[] {"Jueves de música en directo",
                "Sábado, clásico Barça vs Madrid",
                "Domingo, Semifinal UCL Barcelona vs Real Madrid ",
                "Martes de música jazz en directo",};
    }

    public int totalNames() {
        return events.length;
    }
}
