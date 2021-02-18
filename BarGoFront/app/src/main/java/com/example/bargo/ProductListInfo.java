package com.example.bargo;

public class ProductListInfo {

    private int[] images;
    private String[] productos;
    private String[] unidades;
    private int[] points;
    private int[] loQueQueda;
    private int userPoints;
    private static ProductListInfo instance = null;

    public ProductListInfo() {
        setImages();
        setProductos();
        setUnidades();
        setPoints();
        setLoQueQueda();
        userPoints = User.getInstance().getPoints();
    }

    public static ProductListInfo getInstance(){
        if(instance == null)
            instance = new ProductListInfo();
        return instance;
    }

    public int[] getImages() {
        return images;
    }

    public void setImages() {
        this.images = new int[]{R.drawable.image001,
                R.drawable.image002,
                R.drawable.image004,
                R.drawable.image003,};
    }

    public String[] getProductos() {
        return productos;
    }

    public void setProductos() {
        this.productos = new String[] {"Pendrive 64GB",
                "2 Entradas de cine",
                "2 Entradas del picasso",
                "2 Entradas de aquarium"};
    }


    public String[] getUnidades() {
        return unidades;
    }

    public void setUnidades() {
        this.unidades = new String[] {" /1000 ud.",
                " /500 ud.",
                " /500 ud.",
                " /500 ud.",};
    }

    public int[] getPoints() {
        return points;
    }

    public void setPoints() {
        this.points = new int[] {3000,
                6000,
                6500,
                7000,};
    }

    public int[] getLoQueQueda() {
        return loQueQueda;
    }

    public void setLoQueQueda() {
        this.loQueQueda = new int[] {566,
                400,
                342,
                489,};
    }

    public boolean checkValue(int position) {
        if(userPoints < points[position])
            return false;
        else
            return true;
    }
    public void changeValue(int position) {
            this.userPoints -= points[position];
            User.getInstance().setPoints(userPoints);
            this.loQueQueda[position] -= 1;
    }

    public int totalNames() {
        return unidades.length;
    }
}
