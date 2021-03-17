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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
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

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/signup
    private ResponseEntity<?> signupUsuari(@RequestBody SignupUsuari signupUsuari){
        if(usuariService.existsByNomUsuari(signupUsuari.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if(signupUsuari.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.BAD_REQUEST);

        Usuari usuari = new Usuari(signupUsuari.getNomUsuari(), encoder.encode(signupUsuari.getContrasenya()), null);

        Set<Rol> rols = new HashSet<>();
        rols.add(rolService.findByNomRol(NomRol.ROL_USUARI).get());
        if(signupUsuari.getRols().contains("admin"))
            rols.add(rolService.findByNomRol(NomRol.ROL_ADMIN).get());

        usuari.setRols(rols);
        usuariService.save(usuari);

        return new ResponseEntity<>(new Missatge("El usuario se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/login
    public ResponseEntity<JwtDto> loginUsuari(@Valid @RequestBody LoginUsuari loginUsuari, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Missatge("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuari.getNomUsuari(), loginUsuari.getContrasenya()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/refresh", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris/auth/refresh
    public ResponseEntity<JwtDto> refreshToken(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwtDtoRefreshed = new JwtDto(token);

        return new ResponseEntity<>(jwtDtoRefreshed, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ROL_ADMIN')") //PER A INDICAR QUI TE AUTORITZACIO A AQUESTA PETICIO, PERO NO FUNCIONA
    @RequestMapping(method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris
    private ResponseEntity<List<Usuari>> getAllUsuaris(){
        return ResponseEntity.ok(usuariService.findAll());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<GetUsuari> getUsuariById(@PathVariable("id") Long id){ //TODO: cal convertir a BASE64?
        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Usuari usuari = optionalUsuari.get();
        GetUsuari getUsuari = new GetUsuari(usuari.getId(), usuari.getNomUsuari(), usuari.getImatge(), usuari.getRols()); //Creem DTO usuari sense contrasenya

        return new ResponseEntity<>(getUsuari, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/usuaris
    private ResponseEntity<?> updateUsuari(@RequestBody Usuari usuari) throws IOException {
        Optional<Usuari> optionalUsuari = usuariService.findById(usuari.getId());
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Usuari usuariexists = optionalUsuari.get();
        if(usuariService.existsByNomUsuari(usuari.getNomUsuari()) && !usuari.getNomUsuari().equals(usuariexists.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if(usuari.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.BAD_REQUEST);

        usuariexists.setNomUsuari(usuari.getNomUsuari());
        usuariexists.setContrasenya(encoder.encode(usuari.getContrasenya()));

        return new ResponseEntity<>(new Missatge("Se ha actualizado el usuario"), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<?> updateImatgeUsuari(@PathVariable("id") Long id, @RequestBody MultipartFile imatge) throws IOException {
        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Usuari usuari = optionalUsuari.get();

        byte[] bytes = imatge.getBytes();
        usuari.setImatge(bytes);//lo guardamos en la entidad

        Usuari usuariupdated = usuariService.save(usuari);

        /*
        byte[] bytesimatge = usuariupdated.getImatge();

        File f_nou = new File("/Users/annallanza/Documents/Uni/4t/Q2/TFG/fotoperfilcorbata2.jpeg"); //asociamos el archivo fisico
        OutputStream os = new FileOutputStream(f_nou);
        os.write(bytesimatge);
        os.close();
        */

        return new ResponseEntity<>(new Missatge("Se ha actualizado la imagen del usuario"), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<?> deleteUsuariById(@PathVariable("id") Long id) { //TODO: Retornar missatge + controlar errors
        Optional<Usuari> optionalUsuari = usuariService.findById(id);
        if (!optionalUsuari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        usuariService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el usuario"), HttpStatus.OK);
    }

}