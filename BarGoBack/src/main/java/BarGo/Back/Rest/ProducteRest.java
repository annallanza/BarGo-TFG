package BarGo.Back.Rest;

import BarGo.Back.Dto.BescanviarCodi;
import BarGo.Back.Dto.Missatge;
import BarGo.Back.Model.Consumidor;
import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Producte;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.EstablimentService;
import BarGo.Back.Service.ProducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("codis") //Definim la URL del SERVICE (arrel)
public class ProducteRest {

    @Autowired
    private ProducteService producteService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private EstablimentService establimentService;

    @Autowired
    private JwtProvider jwtProvider;

    //AQUESTA NOMES L'UTILITZO JO PER A CREAR PRODUCTES, NO S'UTILITZA A L'APP
    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/codis
    private ResponseEntity<?> createProducte(){
        String prod = "PROD";

        String codiGenerat = UUID.randomUUID().toString();
        codiGenerat = codiGenerat.substring(0,8);
        String codi = prod + codiGenerat;

        while(producteService.existsByCodi(codi)){
            codiGenerat = UUID.randomUUID().toString();
            codiGenerat = codiGenerat.substring(0,8);
            codi = prod + codiGenerat;
        }

        Producte producte = new Producte(codi);

        producteService.save(producte);

        return new ResponseEntity<>(new Missatge("El producto se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "bescanviar", method = RequestMethod.POST) //Exemple url request: http://localhost:8080/codis/bescanviar
    private ResponseEntity<?> bescanviarCodi(@Valid @RequestBody BescanviarCodi bescanviarCodi, BindingResult bindingResult, @RequestHeader(value="Authorization") String token){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(!jwtProvider.validateIdToken(bescanviarCodi.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(bescanviarCodi.getId());
        if(!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();

        if(bescanviarCodi.getCodi().startsWith("EST")) {
            Optional<Establiment> optionalEstabliment = establimentService.findByCodi(bescanviarCodi.getCodi());
            if(!optionalEstabliment.isPresent())
                return new ResponseEntity<>(new Missatge("El código no es válido"), HttpStatus.CONFLICT);

            Establiment establiment = optionalEstabliment.get();

            Set<Establiment> establimentsVisitats = consumidor.getEstablimentsVisitats();

            if(establimentsVisitats.contains(establiment))
                return new ResponseEntity<>(new Missatge("El código ya ha sido canjeado"), HttpStatus.CONFLICT);

            Set<Consumidor> consumidorsVisitants = establiment.getConsumidorsVisitants();
            consumidorsVisitants.add(consumidor);
            establiment.setConsumidorsVisitants(consumidorsVisitants);

            establimentsVisitats.add(establiment);
            consumidor.setEstablimentsVisitats(establimentsVisitats);

            establimentService.save(establiment);
        }
        else {
            Optional<Producte> optionalProducte = producteService.findByCodi(bescanviarCodi.getCodi());
            if(!optionalProducte.isPresent())
                return new ResponseEntity<>(new Missatge("El código no es válido"), HttpStatus.CONFLICT);

            Producte producte = optionalProducte.get();

            Set<Producte> productesBescanviats = consumidor.getProductesBescanviats();

            if(productesBescanviats.contains(producte))
                return new ResponseEntity<>(new Missatge("El código ya ha sido canjeado"), HttpStatus.CONFLICT);

            Set<Consumidor> consumidorsBescanviats = producte.getConsumidorsBescanviats();
            consumidorsBescanviats.add(consumidor);
            producte.setConsumidorsBescanviats(consumidorsBescanviats);

            productesBescanviats.add(producte);
            consumidor.setProductesBescanviats(productesBescanviats);

            producteService.save(producte);
        }

        consumidor.setPuntuacio(consumidor.getPuntuacio() + 100);
        consumidorService.save(consumidor);

        return new ResponseEntity<>(new Missatge("El código se ha canjeado correctamente"), HttpStatus.OK);
    }
}

