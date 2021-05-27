package BarGo.Back.Service;

import BarGo.Back.Model.Repte;
import BarGo.Back.Repository.RepteInterface;
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
public class RepteService implements RepteInterface {

    @Autowired
    private RepteInterface repteInterface;

    @Override
    public List<Repte> findAll() {
        return repteInterface.findAll();
    }

    @Override
    public List<Repte> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Repte> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Repte> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Repte repte) {

    }

    @Override
    public void deleteAll(Iterable<? extends Repte> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Repte> S save(S s) {
        return repteInterface.save(s);
    }

    @Override
    public <S extends Repte> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Repte> findById(Long aLong) {
        return repteInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return repteInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Repte> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Repte> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Repte getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Repte> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Repte> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Repte> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Repte> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Repte> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Repte> boolean exists(Example<S> example) {
        return false;
    }
}
