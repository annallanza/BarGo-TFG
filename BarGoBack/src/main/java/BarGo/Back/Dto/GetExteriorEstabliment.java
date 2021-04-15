package BarGo.Back.Dto;

public class GetExteriorEstabliment {

    private boolean exterior;

    public GetExteriorEstabliment() {
    }

    public GetExteriorEstabliment(boolean exterior) {
        this.exterior = exterior;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }
}
