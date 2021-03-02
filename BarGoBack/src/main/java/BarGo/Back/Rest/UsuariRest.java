package BarGo.Back.Rest;

import BarGo.Back.Model.Usuari;
import BarGo.Back.Service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

}
