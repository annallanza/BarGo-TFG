package BarGo.Back.Rest;

import BarGo.Back.Dto.GetUsuari;
import BarGo.Back.Dto.Missatge;
import BarGo.Back.Dto.SignupConsumidor;
import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Propietari;
import BarGo.Back.Model.Rol;
import BarGo.Back.Service.PropietariService;
import BarGo.Back.Service.RolService;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("propietaris") //Definim la URL del SERVICE (arrel)
public class PropietariRest {

    @Autowired
    private PropietariService propietariService;

    @Autowired
    private UsuariService usuariService;

    @Autowired
    RolService rolService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    //TODO: quan fem establiments, cal fer DTO SignupPropietari
    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/propietaris/auth/signup
    private ResponseEntity<?> signupPropietari(@RequestBody SignupConsumidor signupConsumidor){ //TODO: FER BADREQUEST EN EL CAS DE QUE NO S'INDIQUIN TOTS ELS PARAMETRES
        if(usuariService.existsByNomUsuari(signupConsumidor.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);
        if(signupConsumidor.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.CONFLICT);

        Propietari propietari = new Propietari(signupConsumidor.getNomUsuari(), encoder.encode(signupConsumidor.getContrasenya()), null);

        Set<Rol> rols = new HashSet<>();
        rols.add(rolService.findByNomRol(NomRol.ROL_PROPIETARI).get());

        propietari.setRols(rols);
        propietariService.save(propietari);

        return new ResponseEntity<>(new Missatge("El propietario se ha creado correctamente"), HttpStatus.CREATED);
    }

    //TODO: quan fem establiments, cal fer DTO GetPropietari
    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/propietaris/3
    private ResponseEntity<GetUsuari> getPropietariById(@PathVariable("id") Long id){ //TODO: cal convertir a BASE64?
        Optional<Propietari> optionalPropietari = propietariService.findById(id);
        if (!optionalPropietari.isPresent())
            return new ResponseEntity(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietari = optionalPropietari.get();
        GetUsuari getUsuari = new GetUsuari(propietari.getId(), propietari.getNomUsuari(), propietari.getImatge(), propietari.getRols()); //Creem DTO usuari sense contrasenya

        return new ResponseEntity<>(getUsuari, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/propietaris
    private ResponseEntity<?> updatePropietari(@RequestBody Propietari propietari){
        Optional<Propietari> optionalPropietari = propietariService.findById(propietari.getId());
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietariexists = optionalPropietari.get();
        if(usuariService.existsByNomUsuari(propietari.getNomUsuari()) && !propietari.getNomUsuari().equals(propietariexists.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if(propietari.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.BAD_REQUEST);

        propietariexists.setNomUsuari(propietari.getNomUsuari());
        propietariexists.setContrasenya(encoder.encode(propietari.getContrasenya()));

        Propietari propietariupdated = propietariService.save(propietariexists);

        return new ResponseEntity<>(new Missatge("Se ha actualizado el usuario"), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/propietaris/3
    private ResponseEntity<?> deletePropietariById(@PathVariable("id") Long id) {
        Optional<Propietari> optionalPropietari = propietariService.findById(id);
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun propietario con ese id"), HttpStatus.NOT_FOUND);

        propietariService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el propietario"), HttpStatus.NO_CONTENT);
    }
}
