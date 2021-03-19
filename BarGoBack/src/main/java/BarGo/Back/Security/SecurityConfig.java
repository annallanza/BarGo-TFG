package BarGo.Back.Security;

import BarGo.Back.Security.Jwt.JwtEntryPoint;
import BarGo.Back.Security.Jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //TODO: NO CAL?
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Per indicar a quins metodes pot accedir nomes el que te rol d'ADMIN. Els metodes que no tinguin la notacio, els podran fer tots els ROLS
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override //Spring Security
    public void configure(HttpSecurity http) throws Exception {
        /*
        http.authorizeRequests()
                //.antMatchers("/").permitAll()
                //.antMatchers("/usuaris").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.csrf().disable();

        //http.authorizeRequests().antMatchers("/**").hasRole("USER").and().formLogin();
        */

        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/usuaris/auth/**").permitAll() //la url que conte auth, es acceccible per a tothom
                .antMatchers("/consumidors/auth/**").permitAll() //la url que conte auth, es acceccible per a tothom
                .anyRequest().authenticated() //totes les altres urls, cal estar autenticat
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint) //Qui s'encarrega de llençar l'excepcio UNAUTHORIZED
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //La politica es sense cookies, no guardem l'estat de la sessio, si no que per a cada peticio s'envia el token

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); //Li passa l'usuari al context d'autenticacio
    }
}
