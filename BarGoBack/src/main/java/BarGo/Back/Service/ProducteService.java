package BarGo.Back.Service;

import BarGo.Back.Model.Producte;
import BarGo.Back.Repository.ProducteInterface;
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
public class ProducteService implements ProducteInterface{

    @Autowired
    private ProducteInterface producteInterface;

    @Override
    public List<Producte> findAll() {
        return producteInterface.findAll();
    }

    @Override
    public List<Producte> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Producte> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Producte> findAllById(Iterable<Long> iterable) {
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
    public void delete(Producte producte) {

    }

    @Override
    public void deleteAll(Iterable<? extends Producte> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Producte> S save(S s) {
        return producteInterface.save(s);
    }

    @Override
    public <S extends Producte> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Producte> findById(Long aLong) {
        return producteInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return producteInterface.existsById(aLong);
    }

    @Override
    public Optional<Producte> findByCodi(String codi) {
        return producteInterface.findByCodi(codi);
    }

    @Override
    public boolean existsByCodi(String codi) {
        return producteInterface.existsByCodi(codi);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Producte> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Producte> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Producte getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Producte> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Producte> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Producte> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Producte> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Producte> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Producte> boolean exists(Example<S> example) {
        return false;
    }
}
