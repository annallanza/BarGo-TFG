package BarGo.Back.Service;

import BarGo.Back.Model.Establiment;
import BarGo.Back.Repository.EstablimentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional //Per mantenir la coherencia a la base de dades, per quan hi ha dos accessos simultanis
public class EstablimentService implements EstablimentInterface {

    @Autowired
    private EstablimentInterface establimentInterface;

    @Override
    public List<Establiment> findAll() {
        return establimentInterface.findAll();
    }

    @Override
    public List<Establiment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Establiment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Establiment> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        establimentInterface.deleteById(aLong);
    }

    @Override
    public void delete(Establiment establiment) {

    }

    @Override
    public void deleteAll(Iterable<? extends Establiment> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Establiment> S save(S s) {
        return establimentInterface.save(s);
    }

    @Override
    public <S extends Establiment> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Establiment> findById(Long aLong) {
        return establimentInterface.findById(aLong);
    }

    @Override
    public List<Establiment> findByNomContaining(String nom) {
        return establimentInterface.findByNomContaining(nom);
    }

    @Override
    public List<Establiment> findByDireccioContaining(String direccio) {
        return establimentInterface.findByDireccioContaining(direccio);
    }

    @Override
    public Optional<Establiment> findByCodi(String codi) {
        return establimentInterface.findByCodi(codi);
    }

    @Override
    public boolean existsByCodi(String codi) {
        return establimentInterface.existsByCodi(codi);
    }

    @Override
    public boolean existsById(Long aLong) {
        return establimentInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Establiment> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Establiment> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Establiment getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Establiment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Establiment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Establiment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Establiment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Establiment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Establiment> boolean exists(Example<S> example) {
        return false;
    }
}
