package BarGo.Back.Dto;

public class GetAllEsdeveniments {

    private Long id;
    private String nom;
    private String nomEstabliment;

    public GetAllEsdeveniments(Long id, String nom, String nomEstabliment) {
        this.id = id;
        this.nom = nom;
        this.nomEstabliment = nomEstabliment;
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

    public String getNomEstabliment() {
        return nomEstabliment;
    }

    public void setNomEstabliment(String nomEstabliment) {
        this.nomEstabliment = nomEstabliment;
    }
}
