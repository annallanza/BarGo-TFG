package BarGo.Back.Service;

import BarGo.Back.Model.Premi;
import BarGo.Back.Repository.PremiInterface;
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
public class PremiService implements PremiInterface {

    @Autowired
    private PremiInterface premiInterface;

    @Override
    public List<Premi> findAll() {
        return premiInterface.findAll();
    }

    @Override
    public List<Premi> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Premi> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Premi> findAllById(Iterable<Long> iterable) {
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
    public void delete(Premi premi) {

    }

    @Override
    public void deleteAll(Iterable<? extends Premi> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Premi> S save(S s) {
        return premiInterface.save(s);
    }

    @Override
    public <S extends Premi> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Premi> findById(Long aLong) {
        return premiInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return premiInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Premi> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Premi> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Premi getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Premi> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Premi> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Premi> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Premi> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Premi> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Premi> boolean exists(Example<S> example) {
        return false;
    }
}
