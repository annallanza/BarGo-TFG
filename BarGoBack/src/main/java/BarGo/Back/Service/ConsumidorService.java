package BarGo.Back.Service;

import BarGo.Back.Model.Consumidor;
import BarGo.Back.Repository.ConsumidorInterface;
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
public class ConsumidorService implements ConsumidorInterface{

    @Autowired
    private ConsumidorInterface consumidorInterface;

    @Override
    public List<Consumidor> findAll() {
        return consumidorInterface.findAll();
    }

    @Override
    public List<Consumidor> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Consumidor> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Consumidor> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        consumidorInterface.deleteById(aLong);
    }

    @Override
    public void delete(Consumidor consumidor) {

    }

    @Override
    public void deleteAll(Iterable<? extends Consumidor> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Consumidor> S save(S s) {
        return consumidorInterface.save(s);
    }

    @Override
    public <S extends Consumidor> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Consumidor> findById(Long aLong) {
        return consumidorInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return consumidorInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Consumidor> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Consumidor> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Consumidor getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Consumidor> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Consumidor> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Consumidor> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Consumidor> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Consumidor> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Consumidor> boolean exists(Example<S> example) {
        return false;
    }
}
