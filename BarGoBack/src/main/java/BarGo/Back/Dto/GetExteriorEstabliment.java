package BarGo.Back.Dto;

public class GetExteriorEstabliment {

    private boolean exterior;
    private String ocupacioInterior;
    private String ocupacioExterior;

    public GetExteriorEstabliment() {
    }

    public GetExteriorEstabliment(boolean exterior, String ocupacioInterior, String ocupacioExterior) {
        this.exterior = exterior;
        this.ocupacioInterior = ocupacioInterior;
        this.ocupacioExterior = ocupacioExterior;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }

    public String getOcupacioInterior() {
        return ocupacioInterior;
    }

    public void setOcupacioInterior(String ocupacioInterior) {
        this.ocupacioInterior = ocupacioInterior;
    }

    public String getOcupacioExterior() {
        return ocupacioExterior;
    }

    public void setOcupacioExterior(String ocupacioExterior) {
        this.ocupacioExterior = ocupacioExterior;
    }
}
