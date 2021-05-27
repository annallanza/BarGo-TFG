package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Model.Consumidor;
import BarGo.Back.Model.Repte;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.RepteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("reptes") //Definim la URL del SERVICE (arrel)
public class RepteRest {

    @Autowired
    private RepteService repteService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private JwtProvider jwtProvider;

    //AQUESTA NOMES L'UTILITZO JO PER A CREAR REPTES, NO S'UTILITZA A L'APP
    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/reptes
    private ResponseEntity<?> createRepte(@Valid @RequestBody CreateRepte createRepte, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        Repte repte = new Repte(createRepte.getNom(), createRepte.getPuntuacio());

        repteService.save(repte);

        return new ResponseEntity<>(new Missatge("El reto se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/reptes/3
    private ResponseEntity<?> getAllReptesConsumidorById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);

        if(!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        Consumidor consumidor = optionalConsumidor.get();

        Set<Repte> reptesConsumidor = consumidor.getReptesRealitzats();
        List<Repte> allReptes = repteService.findAll();

        List<GetAllReptes> getAllReptesList = new ArrayList<>();
        for (Repte repte : allReptes) {
            boolean complet = false;
            String progres = "/1";
            if(reptesConsumidor.contains(repte)) {
                complet = true;
                progres = "1" + progres;
            }
            else{
                boolean afegirRepte = false;
                if((repte.getId() == 1 && consumidor.getProductesBescanviats().size() > 0) || (repte.getId() == 2 && consumidor.getEstablimentsVisitats().size() > 0) ||
                        (repte.getId() == 3 && consumidor.getPremisIntercanviats().size() > 0) || (repte.getId() == 4 && consumidor.getReserves().size() > 0) || (repte.getId() == 5 && consumidor.getImatge() != null))
                    afegirRepte = true;

                if(afegirRepte){
                    Set<Consumidor> consumidorsPosseidors = repte.getConsumidorsPosseidors();

                    reptesConsumidor.add(repte);
                    consumidorsPosseidors.add(consumidor);

                    consumidor.setPuntuacio(consumidor.getPuntuacio() + repte.getPuntuacio());

                    repteService.save(repte);
                    consumidorService.save(consumidor);

                    complet = true;
                    progres = "1" + progres;
                }
                else
                    progres = "0" + progres;
            }
            GetAllReptes getAllReptes = new GetAllReptes(repte.getId(), repte.getNom(), repte.getPuntuacio(), complet, progres);
            getAllReptesList.add(getAllReptes);
        }

        return new ResponseEntity<>(getAllReptesList, HttpStatus.OK);
    }
}
