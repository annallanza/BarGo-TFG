package BarGo.Back.Security;


import BarGo.Back.Model.Usuari;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UsuariPrincipal implements UserDetails { //Serveix per a controlar rols/privilegis

    private String nomUsuari;
    private String contrasenya;
    private Collection<?extends GrantedAuthority> authorities;

    public UsuariPrincipal(String nomUsuari, String contrasenya, Collection<? extends GrantedAuthority> authorities) {
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.authorities = authorities;
    }

    public static UsuariPrincipal build (Usuari usuari){
        List<GrantedAuthority> authorities = usuari.getRols().stream().map(rol -> new SimpleGrantedAuthority(rol.getNomRol().name())).collect(Collectors.toList());

        return new UsuariPrincipal(usuari.getNomUsuari(), usuari.getContrasenya(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return nomUsuari;
    }

    @Override
    public String getPassword() {
        return contrasenya;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
