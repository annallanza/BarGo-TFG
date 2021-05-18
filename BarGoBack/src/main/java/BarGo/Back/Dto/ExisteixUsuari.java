package BarGo.Back.Dto;

public class ExisteixUsuari {

    private boolean existeix;

    public ExisteixUsuari() {
    }

    public ExisteixUsuari(boolean existeix) {
        this.existeix = existeix;
    }

    public boolean isExisteix() {
        return existeix;
    }

    public void setExisteix(boolean existeix) {
        this.existeix = existeix;
    }
}
