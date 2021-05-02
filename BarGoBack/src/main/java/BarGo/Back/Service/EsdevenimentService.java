package BarGo.Back.Service;

import BarGo.Back.Model.Esdeveniment;
import BarGo.Back.Repository.EsdevenimentInterface;
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
public class EsdevenimentService implements EsdevenimentInterface {

    @Autowired
    private EsdevenimentInterface esdevenimentInterface;

    @Override
    public List<Esdeveniment> findAll() {
        return esdevenimentInterface.findAll();
    }

    @Override
    public List<Esdeveniment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Esdeveniment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Esdeveniment> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        esdevenimentInterface.deleteById(aLong);
    }

    @Override
    public void delete(Esdeveniment esdeveniment) {
    }

    @Override
    public void deleteAll(Iterable<? extends Esdeveniment> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Esdeveniment> S save(S s) {
        return esdevenimentInterface.save(s);
    }

    @Override
    public <S extends Esdeveniment> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Esdeveniment> findById(Long aLong) {
        return esdevenimentInterface.findById(aLong);
    }

    @Override
    public List<Esdeveniment> findByNomContaining(String nom) {
        return esdevenimentInterface.findByNomContaining(nom);
    }

    public List<Esdeveniment> findByNomEstabliment(String nomEstabliment) {
        List<Esdeveniment> Allesdeveniments = esdevenimentInterface.findAll();

        List<Esdeveniment> esdeveniments = new ArrayList<>();
        for(Esdeveniment esdeveniment : Allesdeveniments) {
            if(esdeveniment.getEstabliment().getNom().contains(nomEstabliment))
                esdeveniments.add(esdeveniment);
        }

        return esdeveniments;
    }

    public List<Esdeveniment> findByDireccioEstabliment(String direccioEstabliment) {
        List<Esdeveniment> Allesdeveniments = esdevenimentInterface.findAll();

        List<Esdeveniment> esdeveniments = new ArrayList<>();
        for(Esdeveniment esdeveniment : Allesdeveniments) {
            if(esdeveniment.getEstabliment().getDireccio().contains(direccioEstabliment))
                esdeveniments.add(esdeveniment);
        }

        return esdeveniments;
    }

    @Override
    public boolean existsById(Long aLong) {
        return esdevenimentInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Esdeveniment> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Esdeveniment> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Esdeveniment getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Esdeveniment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Esdeveniment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Esdeveniment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Esdeveniment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Esdeveniment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Esdeveniment> boolean exists(Example<S> example) {
        return false;
    }
}
