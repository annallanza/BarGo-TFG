package BarGo.Back.Rest;

import BarGo.Back.Dto.JwtDto;
import BarGo.Back.Dto.LoginUsuari;
import BarGo.Back.Dto.Missatge;
import BarGo.Back.Dto.SignupUsuari;
import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Rol;
import BarGo.Back.Model.Usuari;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.RolService;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("usuaris") //Definim la URL del SERVICE (arrel)
//@CrossOrigin //Per accedir des de qualsevol url
public class UsuariRest {

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST)
    private ResponseEntity<?> signupUsuari(@Valid @RequestBody SignupUsuari signupUsuari, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        if(usuariService.existsByNomUsuari(signupUsuari.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);

        Usuari usuari = new Usuari(signupUsuari.getNomUsuari(), encoder.encode(signupUsuari.getContrasenya()));

        Set<Rol> rols = new HashSet<>();
        rols.add(rolService.findByNomRol(NomRol.ROL_USUARI).get());
        if(signupUsuari.getRols().contains("admin"))
            rols.add(rolService.findByNomRol(NomRol.ROL_ADMIN).get());

        usuari.setRols(rols);
        usuariService.save(usuari);

        return new ResponseEntity<>(new Missatge("usuari guardat"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<JwtDto> loginUsuari(@Valid @RequestBody LoginUsuari loginUsuari, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Missatge("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuari.getNomUsuari(), loginUsuari.getContrasenya()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    private ResponseEntity<List<Usuari>> getAllUsuaris(){
        return ResponseEntity.ok(usuariService.findAll());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<Usuari> getUsuariById(@PathVariable("id") Long id){
        Optional<Usuari> optionalusuari = usuariService.findById(id);

        if (optionalusuari.isPresent()) {
            Usuari usuari = optionalusuari.get();
            return ResponseEntity.ok(usuari);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris
    //+ afegir body raw amb format JSON:
    //{
    //    "id": 6, --> Aixo no cal perque es genera automaticament
    //    "nomUsuari": "username",
    //    "contrasenya": "123"
    //}
    private ResponseEntity<Usuari> guardarUsuari(@RequestBody Usuari usuari) { //Si ja existeix no el posa
        try {
            usuari.setContrasenya(encoder.encode(usuari.getContrasenya()));
            Usuari usuariGuardat = usuariService.save(usuari);
            return ResponseEntity.created(new URI("/usuaris/" + usuariGuardat.getId())).body(usuariGuardat);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/products
    //+ afegir body raw amb format JSON:
    //{
    //    "id": 6,
    //    "nomUsuari": "username",
    //    "contrasenya": "123"
    //}
    private ResponseEntity<Usuari> updateUsuari(@RequestBody Usuari usuari) {
        Optional<Usuari> optionalUsuari = usuariService.findById(usuari.getId());
        if (optionalUsuari.isPresent()) {
            Usuari usuariexists = optionalUsuari.get();
            usuariexists.setNomUsuari(usuari.getNomUsuari());
            usuariexists.setContrasenya(encoder.encode(usuari.getContrasenya()));
            Usuari usuariupdated = usuariService.save(usuariexists);
            return ResponseEntity.ok(usuariupdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<Void> deleteUsuariById(@PathVariable("id") Long id) {
        Optional<Usuari> optionalUsuari = usuariService.findById(id);

        if(optionalUsuari.isPresent()) {
            usuariService.deleteById(id);

            Optional<Usuari> optionalUsuariDeleted = usuariService.findById(id);
            if(!optionalUsuariDeleted.isPresent()) return ResponseEntity.ok(null);
            else return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.badRequest().build();
    }

}