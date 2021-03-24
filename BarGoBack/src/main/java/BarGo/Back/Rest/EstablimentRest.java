package BarGo.Back.Rest;

import BarGo.Back.Service.EstablimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("establiments") //Definim la URL del SERVICE (arrel)
public class EstablimentRest {

    @Autowired
    private EstablimentService establimentService;
}
