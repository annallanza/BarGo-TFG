package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Model.*;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.PremiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("premis") //Definim la URL del SERVICE (arrel)
public class PremiRest {

    @Autowired
    private PremiService premiService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private JwtProvider jwtProvider;

    //AQUESTA NOMES L'UTILITZO JO PER A CREAR PRODUCTES, NO S'UTILITZA A L'APP
    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/premis
    private ResponseEntity<?> createPremi(@Valid @RequestBody CreatePremi createPremi, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        Premi premi = new Premi(createPremi.getNom(), createPremi.getPuntuacio());

        premiService.save(premi);

        return new ResponseEntity<>(new Missatge("El premio se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET) //Exemple url request: http://localhost:8080/premis
    private ResponseEntity<?> getAllPremis(){
        List<Premi> allPremis = premiService.findAll();

        List<GetAllPremis> getAllPremisList = new ArrayList<>();
        for (Premi premi : allPremis) {
            GetAllPremis getAllPremis = new GetAllPremis(premi.getId(), premi.getNom(), premi.getPuntuacio());
            getAllPremisList.add(getAllPremis);
        }

        return new ResponseEntity<>(getAllPremisList, HttpStatus.OK);
    }

    @RequestMapping(value = "intercanviar", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/premis/intercanviar
    private ResponseEntity<?> IntercanviarPremi(@Valid @RequestBody IntercanviarPremi intercanviarPremi, BindingResult bindingResult, @RequestHeader(value="Authorization") String token) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if (!jwtProvider.validateIdToken(intercanviarPremi.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(intercanviarPremi.getId());
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();

        Optional<Premi> optionalPremi = premiService.findById(intercanviarPremi.getIdPremi());
        if (!optionalPremi.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún premio con ese id"), HttpStatus.NOT_FOUND);

        Premi premi = optionalPremi.get();

        if(consumidor.getPuntuacio() < premi.getPuntuacio())
            return new ResponseEntity<>(new Missatge("No dispones de suficientes puntos para intercambiar este premio"), HttpStatus.CONFLICT);

        Set<Premi> premisIntercanviats = consumidor.getPremisIntercanviats();

        if (!premisIntercanviats.contains(premi)){
            Set<Consumidor> consumidorsPosseidors = premi.getConsumidorsPosseidors();
            consumidorsPosseidors.add(consumidor);
            premi.setConsumidorsPosseidors(consumidorsPosseidors);

            premisIntercanviats.add(premi);
            consumidor.setPremisIntercanviats(premisIntercanviats);

            premiService.save(premi);
        }

        consumidor.setPuntuacio(consumidor.getPuntuacio() - premi.getPuntuacio());
        consumidorService.save(consumidor);

        return new ResponseEntity<>(new Missatge("El premio se ha intercambiado correctamente"), HttpStatus.OK);
    }
}
