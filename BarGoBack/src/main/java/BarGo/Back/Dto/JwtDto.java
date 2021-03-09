package BarGo.Back.Dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDto { //JWT per retornar al client

    private String token;
    private String bearer = "Bearer";
    private String nomUsuari;
    private Collection<?extends GrantedAuthority> authorities;

    public JwtDto(String token, String nomUsuari, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nomUsuari = nomUsuari;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
