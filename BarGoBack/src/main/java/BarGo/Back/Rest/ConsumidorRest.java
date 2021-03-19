package BarGo.Back.Rest;

import BarGo.Back.Dto.GetPuntuacioConsumidor;
import BarGo.Back.Dto.Missatge;
import BarGo.Back.Dto.PutConsumidor;
import BarGo.Back.Dto.SignupConsumidor;
import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Consumidor;
import BarGo.Back.Model.Rol;
import BarGo.Back.Service.ConsumidorService;
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
@RequestMapping("consumidors") //Definim la URL del SERVICE (arrel)
public class ConsumidorRest {

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private UsuariService usuariService;

    @Autowired
    RolService rolService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/consumidors/3
    private ResponseEntity<GetPuntuacioConsumidor> getPuntuacioConsumidorById(@PathVariable("id") Long id){
        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity(new Missatge("No existe ningun consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();

        GetPuntuacioConsumidor getPuntuacioConsumidor = new GetPuntuacioConsumidor(consumidor.getPuntuacio());

        return new ResponseEntity<>(getPuntuacioConsumidor, HttpStatus.OK);
    }

    //TODO: CREC QUE NO FARA FALTA AQUESTA PETICIO
    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/consumidors
    private ResponseEntity<?> updatePuntuacioConsumidor(@RequestBody PutConsumidor putConsumidor){
        Optional<Consumidor> optionalConsumidor = consumidorService.findById(putConsumidor.getId());
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun usuario con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();
        consumidor.setPuntuacio(putConsumidor.getPuntuacio());

        Consumidor consumidorUpdated = consumidorService.save(consumidor);

        return new ResponseEntity<>(new Missatge("Se ha actualizado la puntuacion del consumidor"), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/consumidors/auth/signup
    private ResponseEntity<?> signupConsumidor(@RequestBody SignupConsumidor signupConsumidor){ //TODO: FER BADREQUEST EN EL CAS DE QUE NO S'INDIQUIN TOTS ELS PARAMETRES
        if(usuariService.existsByNomUsuari(signupConsumidor.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);
        if(signupConsumidor.getContrasenya().replaceAll(" ", "").length() < 8)
            return new ResponseEntity<>(new Missatge("La contraseña no es fiable"), HttpStatus.CONFLICT);

        Consumidor consumidor = new Consumidor(signupConsumidor.getNomUsuari(), encoder.encode(signupConsumidor.getContrasenya()), null, 0);

        Set<Rol> rols = new HashSet<>();
        rols.add(rolService.findByNomRol(NomRol.ROL_CONSUMIDOR).get());

        consumidor.setRols(rols);
        consumidorService.save(consumidor);

        return new ResponseEntity<>(new Missatge("El consumidor se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/consumidors/3
    private ResponseEntity<?> deleteConsumidorById(@PathVariable("id") Long id) {
        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun consumidor con ese id"), HttpStatus.NOT_FOUND);

        consumidorService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el consumidor"), HttpStatus.NO_CONTENT);
    }
}
