package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Enums.NomRol;
import BarGo.Back.Enums.TipusOcupacio;
import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Propietari;
import BarGo.Back.Model.Rol;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Security.Jwt.JwtTokenFilter;
import BarGo.Back.Service.EstablimentService;
import BarGo.Back.Service.PropietariService;
import BarGo.Back.Service.RolService;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("propietaris") //Definim la URL del SERVICE (arrel)
public class PropietariRest {

    @Autowired
    private PropietariService propietariService;

    @Autowired
    private EstablimentService establimentService;

    @Autowired
    private UsuariService usuariService;

    @Autowired
    RolService rolService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/propietaris/auth/signup
    private ResponseEntity<?> signupPropietari(@Valid @RequestBody SignupPropietari signupPropietari, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(usuariService.existsByNomUsuari(signupPropietari.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);

        Establiment establiment = new Establiment(signupPropietari.getNomEstabliment(), signupPropietari.getDireccio(), signupPropietari.isExterior(), signupPropietari.getNumCadires(),
                signupPropietari.getNumTaules(), signupPropietari.getHorari(), signupPropietari.getDescripcio(), signupPropietari.getPaginaWeb(), TipusOcupacio.Vacio, TipusOcupacio.Vacio);

        Propietari propietari = new Propietari(signupPropietari.getNomUsuari(), encoder.encode(signupPropietari.getContrasenya()), null, establiment);

        Set<Rol> rols = new HashSet<>();
        Optional<Rol> optionalRol = rolService.findByNomRol(NomRol.ROL_PROPIETARI);
        if (!optionalRol.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun rol con ese nombre"), HttpStatus.NOT_FOUND);
        Rol rolPropietari = optionalRol.get();
        rols.add(rolPropietari);
        propietari.setRols(rols);

        propietariService.save(propietari);

        establiment.setPropietari(propietari);
        establimentService.save(establiment);

        return new ResponseEntity<>(new Missatge("El propietario se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/propietaris/3
    private ResponseEntity<?> getPropietariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){ //TODO: cal convertir a BASE64?
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(id);
        if(!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietari = optionalPropietari.get();

        byte[] imatgeBytes = propietari.getImatge();
        String imatge;
        if(imatgeBytes == null)
            imatge = "null";
        else
            imatge = Base64.getEncoder().encodeToString(imatgeBytes);

        Optional<Establiment> optionalEstabliment = propietariService.getEstablimentByUsuariId(id);

        if(!optionalEstabliment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun establecimiento para el usuario con ese id"), HttpStatus.NOT_FOUND);

        Establiment establiment = optionalEstabliment.get();

        GetEstabliment getEstabliment = new GetEstabliment(establiment.getId(), establiment.getNom(), establiment.getDireccio(), establiment.isExterior(), establiment.getNumCadires(), establiment.getNumTaules(), establiment.getHorari(), establiment.getDescripcio(), establiment.getPaginaWeb());

        GetPropietari getPropietari = new GetPropietari(propietari.getId(), propietari.getNomUsuari(), imatge, propietari.getRols(), getEstabliment);

        return new ResponseEntity<>(getPropietari, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/propietaris
    private ResponseEntity<?> updatePropietari(@Valid @RequestBody UpdatePropietari updatePropietari, BindingResult bindingResult, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(updatePropietari.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        Optional<Propietari> optionalPropietari = propietariService.findById(updatePropietari.getId());
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietariexists = optionalPropietari.get();
        if(usuariService.existsByNomUsuari(updatePropietari.getNomUsuari()) && !updatePropietari.getNomUsuari().equals(propietariexists.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);

        propietariexists.setNomUsuari(updatePropietari.getNomUsuari());
        propietariexists.setContrasenya(encoder.encode(updatePropietari.getContrasenya()));

        propietariService.save(propietariexists);

        Optional<Establiment> optionalEstabliment = propietariService.getEstablimentByUsuariId(updatePropietari.getId());

        if (!optionalEstabliment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun establecimiento para el usuario con ese id"), HttpStatus.NOT_FOUND);

        Establiment establiment = optionalEstabliment.get();

        establiment.setNom(updatePropietari.getNomEstabliment());
        establiment.setDireccio(updatePropietari.getDireccio());
        establiment.setExterior(updatePropietari.isExterior());
        establiment.setNumCadires(updatePropietari.getNumCadires());
        establiment.setNumTaules(updatePropietari.getNumTaules());
        establiment.setHorari(updatePropietari.getHorari());
        establiment.setDescripcio(updatePropietari.getDescripcio());
        establiment.setPaginaWeb(updatePropietari.getPaginaWeb());

        establimentService.save(establiment);

        return new ResponseEntity<>(new Missatge("Se ha actualizado el consumidor"), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/propietaris/3
    private ResponseEntity<?> deletePropietariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(id);
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun propietario con ese id"), HttpStatus.NOT_FOUND);

        propietariService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el propietario"), HttpStatus.OK);
    }
}
