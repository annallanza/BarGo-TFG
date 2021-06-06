package BarGo.Back.Rest;

import BarGo.Back.Dto.GetPuntuacioConsumidor;
import BarGo.Back.Dto.Missatge;
import BarGo.Back.Dto.PutConsumidor;
import BarGo.Back.Dto.SignupConsumidor;
import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Consumidor;
import BarGo.Back.Model.Rol;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.EmailService;
import BarGo.Back.Service.RolService;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
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
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/consumidors/3
    private ResponseEntity<?> getPuntuacioConsumidorById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();

        GetPuntuacioConsumidor getPuntuacioConsumidor = new GetPuntuacioConsumidor(consumidor.getPuntuacio());

        return new ResponseEntity<>(getPuntuacioConsumidor, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/consumidors/auth/signup
    private ResponseEntity<?> signupConsumidor(@Valid @RequestBody SignupConsumidor signupConsumidor, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(usuariService.existsByNomUsuari(signupConsumidor.getNomUsuari()))
            return new ResponseEntity<>(new Missatge("El nombre de usuario ya existe"), HttpStatus.CONFLICT);

        if(usuariService.existsByCorreu(signupConsumidor.getCorreu()))
            return new ResponseEntity<>(new Missatge("El correo ya esta en uso"), HttpStatus.CONFLICT);

        Consumidor consumidor = new Consumidor(signupConsumidor.getNomUsuari(), signupConsumidor.getCorreu(), encoder.encode(signupConsumidor.getContrasenya()), null, 0);

        Set<Rol> rols = new HashSet<>();
        rols.add(rolService.findByNomRol(NomRol.ROL_CONSUMIDOR).get());

        consumidor.setRols(rols);
        consumidorService.save(consumidor);

        emailService.sendEmail(consumidor.getCorreu(), "BarGo: Nueva cuenta", "Hola " + consumidor.getNomUsuari() + "!" + "\nHas creado una cuenta en la aplicación BarGo. Ahora ya puedes utilizar todas las funcionalidades que ofrece.");

        return new ResponseEntity<>(new Missatge("El consumidor se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/consumidors/3
    private ResponseEntity<?> deleteConsumidorById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        consumidorService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el consumidor"), HttpStatus.OK); //RETORNA OK EN LLOC DE NO_CONTENT PERQUE ANDROID STUDIO HO INTERPRETA COM UN ERROR
    }


    //TODO: CREC QUE NO FARA FALTA AQUESTA PETICIO
    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/consumidors
    private ResponseEntity<?> updatePuntuacioConsumidor(@Valid @RequestBody PutConsumidor putConsumidor, BindingResult bindingResult, @RequestHeader(value="Authorization") String token){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(!jwtProvider.validateIdToken(putConsumidor.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(putConsumidor.getId());
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún usuario con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();
        consumidor.setPuntuacio(putConsumidor.getPuntuacio());

        Consumidor consumidorUpdated = consumidorService.save(consumidor);

        return new ResponseEntity<>(new Missatge("Se ha actualizado la puntuacion del consumidor"), HttpStatus.OK);
    }

}
