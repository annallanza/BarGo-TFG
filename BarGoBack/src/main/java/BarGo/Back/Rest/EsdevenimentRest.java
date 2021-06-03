package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Model.*;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.EmailService;
import BarGo.Back.Service.EsdevenimentService;
import BarGo.Back.Service.EstablimentService;
import BarGo.Back.Service.PropietariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController //Indiquem que aquesta classe sera un SERVICE REST
@RequestMapping("esdeveniments") //Definim la URL del SERVICE (arrel)
public class EsdevenimentRest {

    @Autowired
    private EsdevenimentService esdevenimentService;

    @Autowired
    private EstablimentService establimentService;

    @Autowired
    private PropietariService propietariService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(method = RequestMethod.GET) //Exemple url request: http://localhost:8080/esdeveniments
    private ResponseEntity<?> getAllEsdeveniments(@RequestParam(required = false) Optional<String> nomEsdeveniment, @RequestParam(required = false) Optional<String> nomEstabliment, @RequestParam(required = false) Optional<String> direccio){
        List<Esdeveniment> allEsdeveniments;
        if(nomEsdeveniment.isPresent())
            allEsdeveniments = esdevenimentService.findByNomContaining(nomEsdeveniment.get()); //http://localhost:8080/esdeveniments?nomEstabliment=nomEst
        else if(nomEstabliment.isPresent())
            allEsdeveniments = esdevenimentService.findByNomEstabliment(nomEstabliment.get()); //http://localhost:8080/esdeveniments?direccio=direccio
        else if(direccio.isPresent())
            allEsdeveniments = esdevenimentService.findByDireccioEstabliment(direccio.get()); //http://localhost:8080/esdeveniments?direccio=direccio
        else allEsdeveniments = esdevenimentService.findAll();

        List<GetAllEsdeveniments> getAllEsdevenimentsList = new ArrayList<>();
        for (Esdeveniment esdeveniment : allEsdeveniments) {
            Establiment establiment = esdeveniment.getEstabliment();

            String establimentNom = establiment.getNom();

            GetAllEsdeveniments getAllEsdeveniments = new GetAllEsdeveniments(esdeveniment.getId(), esdeveniment.getNom(), establimentNom);
            getAllEsdevenimentsList.add(getAllEsdeveniments);
        }

        return new ResponseEntity<>(getAllEsdevenimentsList, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET) //Exemple url request: http://localhost:8080/esdeveniments/3
    private ResponseEntity<?> getEsdevenimentById(@PathVariable("id") Long id){
        Optional<Esdeveniment> optionalEsdeveniment = esdevenimentService.findById(id);
        if (!optionalEsdeveniment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún evento con ese id"), HttpStatus.NOT_FOUND);

        Esdeveniment esdeveniment = optionalEsdeveniment.get();

        Establiment establiment = esdeveniment.getEstabliment();

        GetEsdevenimentById getEsdevenimentById = new GetEsdevenimentById(esdeveniment.getId(), esdeveniment.getNom(), establiment.getNom(), establiment.getDireccio(), esdeveniment.getDia().toString(), esdeveniment.getHora().toString());

        return new ResponseEntity<>(getEsdevenimentById, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/esdeveniments
    private ResponseEntity<?> createEsdeveniment(@Valid @RequestBody CreateEsdeveniment createEsdeveniment, BindingResult bindingResult, @RequestHeader(value="Authorization") String token) throws ParseException {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(!jwtProvider.validateIdToken(createEsdeveniment.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(createEsdeveniment.getId());

        if(!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún propietario con ese id"), HttpStatus.NOT_FOUND);

        Propietari propietari = optionalPropietari.get();

        Establiment establiment = propietari.getEstabliment();

        Set<Esdeveniment> llistaEsdeveniments = establiment.getEsdeveniments();

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy"); //El format que rebem es aixi
        Date dia = formatoDelTexto.parse(createEsdeveniment.getDia());
        Time hora = Time.valueOf(createEsdeveniment.getHora());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date diaActual = calendar.getTime();

        if(dia.before(diaActual))
            return new ResponseEntity<>(new Missatge("El día no puede ser anterior al actual"), HttpStatus.BAD_REQUEST);

        if(dia.equals(diaActual)) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            Date horaActual = calendar.getTime();

            String[] hores = createEsdeveniment.getHora().split(":");

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hores[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hores[1]));
            calendar.set(Calendar.SECOND, Integer.parseInt(hores[2]));
            calendar.set(Calendar.MILLISECOND, 0);

            Date hora2 = calendar.getTime();

            if (hora2.before(horaActual))
                return new ResponseEntity<>(new Missatge("La hora no puede ser anterior a la actual"), HttpStatus.BAD_REQUEST);
        }

        if(!estaDinsHorari(establiment, hora))
            return new ResponseEntity<>(new Missatge("El establecimiento no está abierto a esta hora"), HttpStatus.CONFLICT);

        SimpleDateFormat formatText = new SimpleDateFormat("yyyy-MM-dd"); //El format amb el que volem comparar
        String dia_format = formatText.format(dia);

        for(Esdeveniment esdeveniment : llistaEsdeveniments){
            if(esdeveniment.getDia().toString().equals(dia_format) && esdeveniment.getHora().toString().equals(hora.toString()))
                return new ResponseEntity<>(new Missatge("Ya existe un evento para ese día y hora"), HttpStatus.CONFLICT);
        }

        Esdeveniment esdeveniment = new Esdeveniment(createEsdeveniment.getNom(), dia, hora, establiment);

        esdevenimentService.save(esdeveniment);

        emailService.sendEmail(propietari.getCorreu(), "BarGo: Nuevo evento", "Hola " + propietari.getNomUsuari() + "!" + "\nHas creado el evento " + createEsdeveniment.getNom() + " para el " + createEsdeveniment.getDia() + " a las " + createEsdeveniment.getHora().substring(0,5) + ".");

        return new ResponseEntity<>(new Missatge("El evento se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080//esdeveniments/propietari/3
    private ResponseEntity<?> deleteEsdevenimentById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        Optional<Esdeveniment> optionalEsdeveniment = esdevenimentService.findById(id);
        if (!optionalEsdeveniment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún evento con ese id"), HttpStatus.NOT_FOUND);

        Esdeveniment esdeveniment = optionalEsdeveniment.get();
        Propietari propietari = esdeveniment.getEstabliment().getPropietari();
        if(!jwtProvider.validateIdToken(propietari.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al evento con ese id"), HttpStatus.UNAUTHORIZED);

        emailService.sendEmail(propietari.getCorreu(), "BarGo: Evento eliminado", "Hola " + propietari.getNomUsuari() + "!" + "\nHas eliminado el evento " + esdeveniment.getNom() + " que tenías para el " + esdeveniment.getDia() + " a las " + esdeveniment.getHora().toString().substring(0,5) +  ".");

        esdevenimentService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado el evento"), HttpStatus.OK);
    }

    @RequestMapping(value = "/propietari/{id}", method = RequestMethod.GET)
    private ResponseEntity<?> getAllEsdevenimentsPropietariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(id);

        if(!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún propietario con ese id"), HttpStatus.NOT_FOUND);

        Set<Esdeveniment> esdeveniments = optionalPropietari.get().getEstabliment().getEsdeveniments();

        List<GetAllEsdevenimentsPropietari> esdevenimentList = new ArrayList<>();

        for(Esdeveniment esdeveniment : esdeveniments){
            GetAllEsdevenimentsPropietari getAllEsdevenimentsPropietari = new GetAllEsdevenimentsPropietari(esdeveniment.getId(), esdeveniment.getNom(), esdeveniment.getDia().toString(), esdeveniment.getHora().toString());
            esdevenimentList.add(getAllEsdevenimentsPropietari);
        }

        return new ResponseEntity<>(esdevenimentList, HttpStatus.OK);
    }

    private boolean estaDinsHorari(Establiment establiment, Time hora) {
        String horari = establiment.getHorari();

        String[] horarisy = horari.split(" , ");

        ArrayList<Time> horarisDividits = new ArrayList<>();
        for (String s : horarisy) {
            String[] horaris_ = s.split(" - ");

            for (String horaEnHorari : horaris_) {
                horaEnHorari += ":00";
                Time horaEnHorariTime = Time.valueOf(horaEnHorari);
                horarisDividits.add(horaEnHorariTime);
            }
        }

        int i = 0;
        boolean dinsHorari = false;
        while(!dinsHorari && i < horarisDividits.size()){
            horarisDividits.get(i).setTime(horarisDividits.get(i).getTime() - 60000); //1 minut abans d'obrir
            horarisDividits.get(i+1).setTime(horarisDividits.get(i+1).getTime() - 900000); //15 minuts abans de tancar
            if(hora.after(horarisDividits.get(i)) && hora.before(horarisDividits.get(i+1)))
                dinsHorari = true;
            i += 2;
        }
        return dinsHorari;
    }
}
