package BarGo.Back.Service;

import BarGo.Back.Model.Esdeveniment;
import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Propietari;
import BarGo.Back.Model.Reserva;
import BarGo.Back.Repository.EsdevenimentInterface;
import BarGo.Back.Repository.PropietariInterface;
import BarGo.Back.Repository.ReservaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional //Per mantenir la coherencia a la base de dades, per quan hi ha dos accessos simultanis
public class PropietariService implements PropietariInterface {

    @Autowired
    private PropietariInterface propietariInterface;

    @Autowired
    private EsdevenimentInterface esdevenimentInterface;

    @Autowired
    private ReservaInterface reservaInterface;

    @Override
    public List<Propietari> findAll() {
        return propietariInterface.findAll();
    }

    @Override
    public List<Propietari> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Propietari> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Propietari> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

        List<Long> IdEsdeveniments = new ArrayList<>();
        List<Long> IdReserves = new ArrayList<>();
        Optional<Propietari> optionalPropietari = propietariInterface.findById(aLong);
        if(optionalPropietari.isPresent()){
            Propietari propietari = optionalPropietari.get();

            for(Esdeveniment esdeveniment : propietari.getEstabliment().getEsdeveniments()){
                IdEsdeveniments.add(esdeveniment.getId());
            }

            for(Reserva reserva : propietari.getEstabliment().getReserves()){
                IdReserves.add(reserva.getId());
            }
        }

        propietariInterface.deleteById(aLong);

        for(long IdEsdeveniment : IdEsdeveniments){
            esdevenimentInterface.deleteById(IdEsdeveniment);
        }

        for(long IdReserva : IdReserves){
            reservaInterface.deleteById(IdReserva);
        }

    }

    @Override
    public void delete(Propietari propietari) {

    }

    @Override
    public void deleteAll(Iterable<? extends Propietari> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Propietari> S save(S s) {
        return propietariInterface.save(s);
    }

    @Override
    public <S extends Propietari> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Propietari> findById(Long aLong) {
        return propietariInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return propietariInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Propietari> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Propietari> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Propietari getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Propietari> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Propietari> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Propietari> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Propietari> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Propietari> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Propietari> boolean exists(Example<S> example) {
        return false;
    }

    public Optional<Establiment> getEstablimentByUsuariId(Long id) {
        return propietariInterface.getEstablimentByUsuariId(id);
    }
}
