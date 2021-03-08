package BarGo.Back.Rest;

import BarGo.Back.Model.Usuari;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("usuaris") //Definim la URL del SERVICE (arrel)
public class UsuariRest {

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(method = RequestMethod.GET)
    private ResponseEntity<List<Usuari>> getAllUsuaris(){
        return ResponseEntity.ok(usuariService.findAll());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<Usuari> getUsuariById(@PathVariable("id") Long id){
        Optional<Usuari> optionalusuari = usuariService.findById(id);

        if (optionalusuari.isPresent()) {
            Usuari usuari = optionalusuari.get();
            return ResponseEntity.ok(usuari);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/usuaris
    //+ afegir body raw amb format JSON:
    //{
    //    "id": 6, --> Aixo no cal perque es genera automaticament
    //    "nom": "username",
    //    "contrasenya": "123"
    //}
    private ResponseEntity<Usuari> guardarUsuari(@RequestBody Usuari usuari) { //Si ja existeix no el posa
        try {
            usuari.setContrasenya(encoder.encode(usuari.getContrasenya()));
            Usuari usuariGuardat = usuariService.save(usuari);
            return ResponseEntity.created(new URI("/usuaris/" + usuariGuardat.getId())).body(usuariGuardat);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT) //Exemple url request: http://localhost:8080/products
    //+ afegir body raw amb format JSON:
    //{
    //    "id": 6,
    //    "nom": "username",
    //    "contrasenya": "123"
    //}
    private ResponseEntity<Usuari> updateUsuari(@RequestBody Usuari usuari) {
        Optional<Usuari> optionalUsuari = usuariService.findById(usuari.getId());
        if (optionalUsuari.isPresent()) {
            Usuari usuariexists = optionalUsuari.get();
            usuariexists.setNom(usuari.getNom());
            usuariexists.setContrasenya(encoder.encode(usuari.getContrasenya()));
            Usuari usuariupdated = usuariService.save(usuariexists);
            return ResponseEntity.ok(usuariupdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080/usuaris/3
    private ResponseEntity<Void> deleteUsuariById(@PathVariable("id") Long id) {
        Optional<Usuari> optionalUsuari = usuariService.findById(id);

        if(optionalUsuari.isPresent()) {
            usuariService.deleteById(id);

            Optional<Usuari> optionalUsuariDeleted = usuariService.findById(id);
            if(!optionalUsuariDeleted.isPresent()) return ResponseEntity.ok(null);
            else return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.badRequest().build();
    }

}