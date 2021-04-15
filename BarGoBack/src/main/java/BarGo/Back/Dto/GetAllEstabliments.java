package BarGo.Back.Dto;

public class GetAllEstabliments {

    private Long id;
    private String nom;
    private String imatge;
    private String direccio;
    private boolean visitat;

    public GetAllEstabliments() {
    }

    public GetAllEstabliments(Long id, String nom, String imatge, String direccio, boolean visitat) {
        this.id = id;
        this.nom = nom;
        this.imatge = imatge;
        this.direccio = direccio;
        this.visitat = visitat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public boolean isVisitat() {
        return visitat;
    }

    public void setVisitat(boolean visitat) {
        this.visitat = visitat;
    }
}
