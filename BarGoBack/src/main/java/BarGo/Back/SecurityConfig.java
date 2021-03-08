package BarGo.Back;

import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //TODO: NO CAL?
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UsuariService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override //Spring Security
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* PER FER-HO AMB USUARIS AFEGITS HAND CODED
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("USER","ADMIN");
         */
        //PER FER-HO AMB USUARIS DE LA BASE DE DADES
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override //Spring Security
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/").permitAll()
                //.antMatchers("/usuaris").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.csrf().disable(); //TODO: mirar de treure-ho i que funcioni els posts

        //http.authorizeRequests().antMatchers("/**").hasRole("USER").and().formLogin();;
    }
}
