package BarGo.Back.Dto;

import javax.validation.constraints.NotEmpty;

public class JwtDto { //JWT per retornar al client

    @NotEmpty(message = "El token no puede ser un valor nulo ni vacío")
    private String token;

    public JwtDto() {
    }

    public JwtDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
