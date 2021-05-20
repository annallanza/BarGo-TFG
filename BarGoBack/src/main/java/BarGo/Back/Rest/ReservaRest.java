package BarGo.Back.Rest;

import BarGo.Back.Dto.*;
import BarGo.Back.Model.*;
import BarGo.Back.Security.Jwt.JwtProvider;
import BarGo.Back.Service.ConsumidorService;
import BarGo.Back.Service.EstablimentService;
import BarGo.Back.Service.PropietariService;
import BarGo.Back.Service.ReservaService;
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
@RequestMapping("reserves") //Definim la URL del SERVICE (arrel)
public class ReservaRest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private PropietariService propietariService;

    @Autowired
    private EstablimentService establimentService;

    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(method = RequestMethod.POST) //Exemple url request: http://localhost:8080/reserves
    private ResponseEntity<?> createReserva(@Valid @RequestBody CreateReserva createReserva, BindingResult bindingResult, @RequestHeader(value="Authorization") String token) throws ParseException {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Missatge(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);

        if(!jwtProvider.validateIdToken(createReserva.getId(), token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(createReserva.getId());
        if(!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún usuario consumidor con ese id"), HttpStatus.NOT_FOUND);
        Consumidor consumidor = optionalConsumidor.get();

        Optional<Establiment> optionalEstabliment = establimentService.findById(createReserva.getIdEstabliment());
        if(!optionalEstabliment.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún establecimiento con ese id"), HttpStatus.NOT_FOUND);
        Establiment establiment = optionalEstabliment.get();

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy"); //El format que rebem es aixi
        Date dia = formatoDelTexto.parse(createReserva.getDia());
        Time hora = Time.valueOf(createReserva.getHora());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date diaActual = calendar.getTime();

        if(dia.before(diaActual))
            return new ResponseEntity<>(new Missatge("El día no puede ser anterior al actual"), HttpStatus.BAD_REQUEST);

        if(dia.equals(diaActual)){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            Date horaActual = calendar.getTime();

            String[] hores = createReserva.getHora().split(":");

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hores[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hores[1]));
            calendar.set(Calendar.SECOND, Integer.parseInt(hores[2]));
            calendar.set(Calendar.MILLISECOND, 0);

            Date hora2 = calendar.getTime();

            if (hora2.before(horaActual))
                return new ResponseEntity<>(new Missatge("La hora no puede ser anterior a la actual"), HttpStatus.BAD_REQUEST);
        }

        Set<Reserva> llistaReservesConsumidor = consumidor.getReserves();

        SimpleDateFormat formatText = new SimpleDateFormat("yyyy-MM-dd"); //El format amb el que volem comparar
        String dia_format = formatText.format(dia);

        for(Reserva reserva : llistaReservesConsumidor){
            if(reserva.getDia().toString().equals(dia_format) && reserva.getHora().toString().equals(hora.toString()))
                return new ResponseEntity<>(new Missatge("Ya tienes una reserva para ese día y hora"), HttpStatus.CONFLICT);
        }

        if(createReserva.isExterior() && !establiment.isExterior())
            return new ResponseEntity<>(new Missatge("El establecimiento no ofrece reservas en el exterior"), HttpStatus.CONFLICT);

        if(!estaDinsHorari(establiment, hora))
            return new ResponseEntity<>(new Missatge("El establecimiento no está abierto a esta hora"), HttpStatus.CONFLICT);

        Set<Reserva> llistaReservesEstabliment = establiment.getReserves();
        int numTaules = establiment.getNumTaules();
        Time horaAbans = new Time(hora.getTime() - 3600000);
        Time horaDespres = new Time(hora.getTime() + 3600000);

        int numTotalTaules = createReserva.getNumPersones() / 4;
        if(createReserva.getNumPersones()%4 != 0)
            ++numTotalTaules;

        for(Reserva reserva : llistaReservesEstabliment){ //per a cada reserva de l'establiment, mirar els que estan aquell dia, una hora abans i una hora despres de la hora de reserva
            Time horaReserva = reserva.getHora();
            Date diaReserva = reserva.getDia();
            if(dia.equals(diaReserva)){
                if (horaReserva.after(horaAbans) && horaReserva.before(horaDespres)){
                    numTotalTaules += reserva.getNumPersones() / 4;
                    if (reserva.getNumPersones() % 4 != 0)
                        ++numTotalTaules;
                }
            }
        }

        if(numTotalTaules > numTaules)//si la suma de les taules necessaries de cadascuna d'aquestes reserves + numTaules necesaries per la nova reserva, supera el total de numTaules, return missatge ple
            return new ResponseEntity<>(new Missatge("No se puede hacer una reserva para ese día y hora, el establecimiento está lleno"), HttpStatus.CONFLICT);

        Reserva reserva = new Reserva(dia, hora, createReserva.getNumPersones(), createReserva.isExterior(), establiment, consumidor);

        reservaService.save(reserva);

        return new ResponseEntity<>(new Missatge("La reserva se ha creado correctamente"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE) //Exemple url request: http://localhost:8080//reserves/3
    private ResponseEntity<?> deleteReservaById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        Optional<Reserva> optionalReserva = reservaService.findById(id);
        if (!optionalReserva.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ninguna reserva con ese id"), HttpStatus.NOT_FOUND);

        Reserva reserva = optionalReserva.get();
        long idConsumidor = reserva.getConsumidor().getId();
        long idPropietari = reserva.getEstabliment().getPropietari().getId();
        if(!jwtProvider.validateIdToken(idConsumidor, token) && !jwtProvider.validateIdToken(idPropietari, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso a la reserva con ese id"), HttpStatus.UNAUTHORIZED);

        reservaService.deleteById(id);

        return new ResponseEntity<>(new Missatge("Se ha eliminado la reserva"), HttpStatus.OK);
    }

    @RequestMapping(value = "/consumidor/{id}", method = RequestMethod.GET)
    private ResponseEntity<?> getAllReservesConsumidorById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Consumidor> optionalConsumidor = consumidorService.findById(id);

        if(!optionalConsumidor.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún consumidor con ese id"), HttpStatus.NOT_FOUND);

        Set<Reserva> reserves = optionalConsumidor.get().getReserves();

        List<GetAllReservesConsumidor> reservesList = new ArrayList<>();

        for(Reserva reserva : reserves){
            Establiment establiment = reserva.getEstabliment();
            byte[] imatgeBytes = establiment.getPropietari().getImatge();
            String imatge;
            if (imatgeBytes == null)
                imatge = "null";
            else
                imatge = Base64.getEncoder().encodeToString(imatgeBytes);

            GetAllReservesConsumidor getAllReservesConsumidor = new GetAllReservesConsumidor(reserva.getId(), establiment.getNom(), imatge, establiment.getDireccio(), reserva.getDia().toString(), reserva.getHora().toString(), reserva.getNumPersones(), reserva.isExterior());
            reservesList.add(getAllReservesConsumidor);
        }

        return new ResponseEntity<>(reservesList, HttpStatus.OK);
    }

    @RequestMapping(value = "/propietari/{id}", method = RequestMethod.GET)
    private ResponseEntity<?> getAllReservesPropietariById(@PathVariable("id") Long id, @RequestHeader(value="Authorization") String token){
        if(!jwtProvider.validateIdToken(id, token))
            return new ResponseEntity<>(new Missatge("No tienes acceso al usuario con ese id"), HttpStatus.UNAUTHORIZED);

        Optional<Propietari> optionalPropietari = propietariService.findById(id);

        if(!optionalPropietari.isPresent())
            return new ResponseEntity<>(new Missatge("No existe ningún propietario con ese id"), HttpStatus.NOT_FOUND);

        Set<Reserva> reserves = optionalPropietari.get().getEstabliment().getReserves();

        List<GetAllReservesPropietari> reservesList = new ArrayList<>();
        for(Reserva reserva : reserves){
            GetAllReservesPropietari getAllReservesPropietari = new GetAllReservesPropietari(reserva.getId(), reserva.getConsumidor().getNomUsuari(), reserva.getDia().toString(), reserva.getHora().toString(), reserva.getNumPersones(), reserva.isExterior());
            reservesList.add(getAllReservesPropietari);
        }

        return new ResponseEntity<>(reservesList, HttpStatus.OK);
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
