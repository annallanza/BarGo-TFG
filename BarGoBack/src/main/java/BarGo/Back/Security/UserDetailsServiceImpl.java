package BarGo.Back.Security;

import BarGo.Back.Model.Usuari;
import BarGo.Back.Repository.UsuariInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired //Serveix per evitar posar new UsuariInterface...
    private UsuariInterface usuariInterface;

    @Override //Spring Security
    public UserDetails loadUserByUsername(String nomUsuari) throws UsernameNotFoundException {
        Usuari usuari = usuariInterface.findByNomUsuari(nomUsuari).get();

        /*
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ADMIN"));
        roles.add(new SimpleGrantedAuthority("USER"));

        UserDetails userDetails = new User(usuari.getNomUsuari(), usuari.getContrasenya(), roles);
        return userDetails;
        */
        return UsuariPrincipal.build(usuari);
    }
}
