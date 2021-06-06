package BarGo.Back.Dto;

import javax.validation.constraints.*;

public class CreateReserva {

    @NotNull(message = "El id no puede ser un valor nulo")
    private Long id;

    @NotNull(message = "El id del establecimiento no puede ser un valor nulo")
    private Long idEstabliment;

    @NotBlank(message = "El día no puede ser un valor nulo ni vacío")
    @Pattern(regexp = "(\\d{2})/(\\d{2})/(\\d{4})", message = "La fecha debe tener el formato dd/mm/yyyy")
    private String dia;

    @NotBlank(message = "La hora no puede ser un valor nulo ni vacío")
    @Pattern(regexp = "(\\d{2}):(\\d{2}):(\\d{2})", message = "La hora debe tener el formato hh:mm:ss")
    private String hora;

    @Positive(message = "El número de personas no puede ser un valor negativo")
    private int numPersones;

    private boolean exterior;

    public CreateReserva() {
    }

    public CreateReserva(@NotNull(message = "El id no puede ser un valor nulo") Long id, @NotNull(message = "El id del establecimiento no puede ser un valor nulo") Long idEstabliment, @NotBlank(message = "El día no puede ser un valor nulo ni vacío") @Pattern(regexp = "(\\d{2})/(\\d{2})/(\\d{4})", message = "La fecha debe tener el formato dd/mm/yyyy") String dia, @NotBlank(message = "La hora no puede ser un valor nulo ni vacío") String hora, @Positive(message = "El numero de personas no puede ser un valor negativo") int numPersones, @NotNull(message = "Exterior no puede ser un valor nulo") boolean exterior) {
        this.id = id;
        this.idEstabliment = idEstabliment;
        this.dia = dia;
        this.hora = hora;
        this.numPersones = numPersones;
        this.exterior = exterior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEstabliment() {
        return idEstabliment;
    }

    public void setIdEstabliment(Long idEstabliment) {
        this.idEstabliment = idEstabliment;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getNumPersones() {
        return numPersones;
    }

    public void setNumPersones(int numPersones) {
        this.numPersones = numPersones;
    }

    public boolean isExterior() {
        return exterior;
    }

    public void setExterior(boolean exterior) {
        this.exterior = exterior;
    }
}
