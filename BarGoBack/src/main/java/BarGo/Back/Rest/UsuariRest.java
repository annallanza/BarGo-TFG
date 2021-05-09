package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.text.ParseException;
import java.util.*;

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

    //TODO: NO FARA FALTA AQUESTA PETICIO
    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/signup
    private ResponseEntity<?> signupUsuari(@Valid @RequestBody SignupUsuari signupUsuari){
        if(usuariService.existsByNomUsuari(signupUsuari.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if(signupUsuari.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.BAD_REQUEST);

        Usuari usuari = new Usuari(signupUsuari.getNomUsuari(), signupUsuari.getCorreu(), encoder.encode(signupUsuari.getContrasenya()), null);

        Set<Rol> rols = new HashSet<>();
        if(signupUsuari.getRols().contains("consumidor"))
            rols.add(rolService.findByNomRol(NomRol.ROL_CONSUMIDOR).get());
        if(signupUsuari.getRols().contains("propietari"))
            rols.add(rolService.findByNomRol(NomRol.ROL_PROPIETARI).get());

        usuari.setRols(rols);
        usuariService.save(usuari);

        /* TODO:
        if(signupUsuari.getRols().contains("consumidor"))

        Si rol = ROL_CONSUMIDOR --> CRIDEM A LA FUNCIO QUE CREA UN CONSUMIDOR
        SI rol = ROL_PROPIETARI --> CRIDEM A LA FUNCIO QUE CREA UN PROPIETARI
         */

        return new ResponseEntity<>(new Missatge("El usuario se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/login
    public ResponseEntity<?> loginUsuari(@Valid @RequestBody LoginUsuari loginUsuari, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuari.getNomUsuari(), loginUsuari.getContrasenya()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/refresh", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/refresh
    public ResponseEntity<?> refreshToken(@Valid @RequestBody JwtDto jwtDto, BindingResult bindingResult) throws ParseException {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwtDtoRefreshed = new JwtDto(token);

        return new ResponseEntity<>(jwtDtoRefreshed, HttpStatus.OK);
    }

    //TODO: NO FA FALTA
    //@PreAuthorize("hasRole('ROL_ADMIN')") //PER A INDICAR QUI TE AUTORITZACIO A AQUESTA PETICIO, PERO NO FUNCIONA
    @RequestMapping(method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris
    private ResponseEntity<List<Usuari>> getAllUsuaris(){
        return ResponseEntity.ok(usuariService.findAll());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<?> getUsuariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token) throws IOException {
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Usuari usuari = optionalUsuari.get();

        byte[] imatgeBytes = usuari.getImatge();
        String imatge;
        if(imatgeBytes == null)
            imatge = "null";
        else
             imatge = Base64.getEncoder().encodeToString(imatgeBytes);

        GetUsuari getUsuari = new GetUsuari(usuari.getId(), usuari.getNomUsuari(), usuari.getCorreu(), imatge, usuari.getRols()); //Creem DTO usuari sense contrasenya

        return new ResponseEntity<>(getUsuari, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/usuaris
    private ResponseEntity<?> updateUsuari(@Valid @RequestBody UpdateUsuari updateUsuari, BindingResult bindingResult, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(updateUsuari.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        Optional<Usuari> optionalUsuari = usuariService.findById(updateUsuari.getId());
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Usuari usuariexists = optionalUsuari.get();
        if(usuariService.existsByNomUsuari(updateUsuari.getNomUsuari()) && !updateUsuari.getNomUsuari().equals(usuariexists.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);

        if(usuariService.existsByCorreu(updateUsuari.getCorreu()) && !updateUsuari.getCorreu().equals(usuariexists.getCorreu()))
            return new ResponseEntity<>(new Missatge("El correo ya esta en uso"), HttpStatus.CONFLICT);

        usuariexists.setNomUsuari(updateUsuari.getNomUsuari());
        usuariexists.setCorreu(updateUsuari.getCorreu());
        usuariexists.setContrasenya(encoder.encode(updateUsuari.getContrasenya()));

        Usuari usuariupdated = usuariService.save(usuariexists);

        return new ResponseEntity<>(new Missatge("Se ha actualizado el usuario"), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<?> updateImatgeUsuari(@PathVariable("id") Long id, @RequestParam String imatge, @RequestHeader(value="Authorization") String token) throws IOException {
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>("No tienes acceso al usuario con ese id", HttpStatus.UNAUTHORIZED);

        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>("No existe ningun usuario con ese id", HttpStatus.NOT_FOUND);

        Usuari usuari = optionalUsuari.get();

        byte[] bytes = Base64.getDecoder().decode(imatge);
        usuari.setImatge(bytes);//lo guardamos en la entidad

        Usuari usuariupdated = usuariService.save(usuari);

        /*
        byte[] bytesimatge = usuariupdated.getImatge();

        File f_nou = new File("/Users/annallanza/Documents/Uni/4t/Q2/TFG/fotoGos.jpeg"); //asociamos el archivo fisico
        OutputStream os = new FileOutputStream(f_nou);
        os.write(bytesimatge);
        os.close();

         */

        return new ResponseEntity<>("Se ha actualizado la imagen del usuario", HttpStatus.OK);
    }

    //TODO: CREC QUE NO FARA FALTA AQUESTA PETICIO
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<?> deleteUsuariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        usuariService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el usuario"), HttpStatus.OK);
    }

}