package BarGo.Back.Dto;

import javax.validation.constraints.*;

public class SignupPropietari {

    @NotBlank(message = "El nombre de usuario no puede ser un valor nulo ni vacío")
    private String nomUsuari;

    @NotBlank(message = "El correo electrónico no puede ser un valor nulo ni vacío")
    @Email(message = "El correo electrónico debe tener el formato correcto")
    private String correu;

    @Size(min = 8, message = "La contraseña debe tener un mínimo de 8 caracteres")
    @NotBlank(message = "La contraseña no puede ser un valor nulo ni vacío")
    private String contrasenya;

    @NotBlank(message = "El nombre del establecimiento no puede ser un valor nulo ni vacío")
    private String nomEstabliment;

    @NotBlank(message = "La dirección del establecimiento no puede ser un valor nulo ni vacío")
    private String direccio;

    private boolean exterior;

    @PositiveOrZero(message = "El numero de sillas no puede ser un valor negativo")
    private int numCadires;

    @PositiveOrZero(message = "El numero de mesas no puede ser un valor negativo")
    private int numTaules;

    @NotBlank(message = "El horario del establecimiento no puede ser un valor nulo ni vacío")
    @Pattern(regexp = "((\\d{2}):(\\d{2}) - (\\d{2}):(\\d{2})){1}( , ((\\d{2}):(\\d{2}) - (\\d{2}):(\\d{2})){1}( , ((\\d{2}):(\\d{2}) - (\\d{2}):(\\d{2})){1})?)?", message = "El horario debe tener el formato hh:mm - hh:mm , hh:mm - hh:mm , hh:mm - hh:mm")
    private String horari;

    @Size(min = 200, message = "La descripción del establecimiento ha de tener un mínimo de 200 caracteres")
    @NotBlank(message = "La descripción del establecimiento no puede ser un valor nulo ni vacío")
    private String descripcio;

    private String paginaWeb;

    public SignupPropietari() {
    }

    public SignupPropietari(@NotEmpty(message = "El nombre de usuario no puede ser un valor nulo ni vacío") String nomUsuari, @Size(min = 8, message = "La contraseña debe tener un mínimo de 8 caracteres") @NotNull(message = "La contraseña no puede ser un valor nulo") @NotBlank(message = "La contraseña no puede ser un valor vacío") String contrasenya, @NotEmpty(message = "El nombre del establecimiento no puede ser un valor nulo ni vacío") String nomEstabliment, @NotEmpty(message = "La dirección del establecimiento no puede ser un valor nulo ni vacío") String direccio, @NotNull(message = "Exterior no puede ser un valor nulo") boolean exterior, @PositiveOrZero(message = "El numero de sillas no puede ser un valor negativo") int numCadires, @PositiveOrZero(message = "El numero de mesas no puede ser un valor negativo") int numTaules, @NotEmpty(message = "El horario del establecimiento no puede ser un valor nulo ni vacío") String horari, @NotEmpty(message = "La descripción del establecimiento no puede ser un valor nulo ni vacío") String descripcio, @NotBlank(message = "La pagina web no puede ser un valor vacío") String paginaWeb) {
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.nomEstabliment = nomEstabliment;
        this.direccio = direccio;
        this.exterior = exterior;
        this.numCadires = numCadires;
        this.numTaules = numTaules;
        this.horari = horari;
        this.descripcio = descripcio;
        this.paginaWeb = paginaWeb;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getNomEstabliment() {
        return nomEstabliment;
    }

    public void setNomEstabliment(String nomEstabliment) {
        this.nomEstabliment = nomEstabliment;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }

    public int getNumCadires() {
        return numCadires;
    }

    public void setNumCadires(int numCadires) {
        this.numCadires = numCadires;
    }

    public int getNumTaules() {
        return numTaules;
    }

    public void setNumTaules(int numTaules) {
        this.numTaules = numTaules;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }
}
