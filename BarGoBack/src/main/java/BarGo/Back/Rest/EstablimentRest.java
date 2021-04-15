package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Enums.TipusOcupacio;
import BarGo.Back.Model.Consumidor;
import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Propietari;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.EstablimentService;
import BarGo.Back.Service.PropietariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("establiments") //Definim la URL del SERVICE (arrel)
public class EstablimentRest {

    @Autowired
    private EstablimentService establimentService;

    @Autowired
    private PropietariService propietariService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/establiments/all/14
    private ResponseEntity<?> getAllEstabliments(@PathVariable("id") Long id, @RequestParam(required = false) Optional<String> nomEstabliment, @RequestParam(required = false) Optional<String> direccio){
        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);
        if (!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();
        Set<Establiment> establimentsVisitats = consumidor.getEstablimentsVisitats();

        List<Establiment> allEstabliments;
        if(nomEstabliment.isPresent())
            allEstabliments = establimentService.findByNomContaining(nomEstabliment.get()); //http://localhost:8080/establiments/all/1?nomEstabliment=nomEst
        else if(direccio.isPresent())
            allEstabliments = establimentService.findByDireccioContaining(direccio.get()); //http://localhost:8080/establiments/all/1?direccio=direccio
        else allEstabliments = establimentService.findAll();

        List<GetAllEstabliments> getAllEstablimentsList = new ArrayList<>();
        for (Establiment establiment : allEstabliments) {
            Propietari propietari = establiment.getPropietari();

            byte[] imatgeBytes = propietari.getImatge();
            String imatge;
            if (imatgeBytes == null)
                imatge = "null";
            else
                imatge = Base64.getEncoder().encodeToString(imatgeBytes);

            boolean visitat = false;
            if (establimentsVisitats.contains(establiment))
                visitat = true;

            GetAllEstabliments getAllEstabliments = new GetAllEstabliments(establiment.getId(), establiment.getNom(), imatge, establiment.getDireccio(), visitat);
            getAllEstablimentsList.add(getAllEstabliments);
        }

        return new ResponseEntity<>(getAllEstablimentsList, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/establiments/3
    private ResponseEntity<?> getEstablimentById(@PathVariable("id") Long id){
        Optional<Establiment> optionalEstabliment = establimentService.findById(id);
        if (!optionalEstabliment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun establecimiento con ese id"), HttpStatus.NOT_FOUND);

        Establiment establiment = optionalEstabliment.get();

        Propietari propietari = establiment.getPropietari();

        byte[] imatgeBytes = propietari.getImatge();
        String imatge;
        if(imatgeBytes == null)
            imatge = "null";
        else
            imatge = Base64.getEncoder().encodeToString(imatgeBytes);

        GetEstablimentById getEstablimentById = new GetEstablimentById(establiment.getId(), establiment.getNom(), imatge, establiment.getDireccio(), establiment.getHorari(), establiment.getDescripcio(), establiment.getPaginaWeb(), establiment.getOcupacioInterior(), establiment.getOcupacioExterior());

        return new ResponseEntity<>(getEstablimentById, HttpStatus.OK);
    }

    @RequestMapping(value = "/exterior/{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/establiments/exterior/3
    private ResponseEntity<?> getExteriorEstablimentByIdPropietari(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(id);
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun propietario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietari = optionalPropietari.get();

        Establiment establiment = propietari.getEstabliment();

        GetExteriorEstabliment getExteriorEstabliment = new GetExteriorEstabliment(establiment.isExterior());

        return new ResponseEntity<>(getExteriorEstabliment, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/establiments
    private ResponseEntity<?> updateOcupacioEstablimentByIdPropietari(@Valid @RequestBody UpdateOcupacio updateOcupacio, BindingResult bindingResult, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(updateOcupacio.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        Optional<Propietari> optionalPropietari = propietariService.findById(updateOcupacio.getId());
        if (!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningun propietario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietari = optionalPropietari.get();
        Establiment establiment = propietari.getEstabliment();

        TipusOcupacio ocupacioInterior;
        switch (updateOcupacio.getOcupacioInterior()) {
            case "Lleno":
                ocupacioInterior = TipusOcupacio.Lleno;
                break;
            case "Medio_lleno":
                ocupacioInterior = TipusOcupacio.Medio_lleno;
                break;
            case "Vacio":
                ocupacioInterior = TipusOcupacio.Vacio;
                break;
            default:
                return new ResponseEntity<>(new Missatge("La ocupacion interior no es vàlida"), HttpStatus.BAD_REQUEST);
        }

        TipusOcupacio ocupacioExterior;
        switch (updateOcupacio.getOcupacioExterior()) {
            case "Lleno":
                ocupacioExterior = TipusOcupacio.Lleno;
                break;
            case "Medio_lleno":
                ocupacioExterior = TipusOcupacio.Medio_lleno;
                break;
            case "Vacio":
                ocupacioExterior = TipusOcupacio.Vacio;
                break;
            default:
                return new ResponseEntity<>(new Missatge("La ocupacion exterior no es vàlida"), HttpStatus.BAD_REQUEST);
        }

        establiment.setOcupacioInterior(ocupacioInterior);
        establiment.setOcupacioExterior(ocupacioExterior);

        establimentService.save(establiment);

        return new ResponseEntity<>(new Missatge("Se ha actualizado el establecimiento"), HttpStatus.OK);
    }
}
