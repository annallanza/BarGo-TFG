package BarGo.Back.Service;

import BarGo.Back.Model.Reserva;
import BarGo.Back.Repository.ReservaInterface;
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
@Transactional
public class ReservaService implements ReservaInterface {

    @Autowired
    private ReservaInterface reservaInterface;

    @Override
    public List<Reserva> findAll() {
        return reservaInterface.findAll();
    }

    @Override
    public List<Reserva> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Reserva> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Reserva> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        reservaInterface.deleteById(aLong);
    }

    @Override
    public void delete(Reserva reserva) {
        reservaInterface.delete(reserva);
    }

    @Override
    public void deleteAll(Iterable<? extends Reserva> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Reserva> S save(S s) {
        return reservaInterface.save(s);
    }

    @Override
    public <S extends Reserva> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Reserva> findById(Long aLong) {
        return reservaInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return reservaInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Reserva> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Reserva> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Reserva getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Reserva> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Reserva> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Reserva> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Reserva> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Reserva> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Reserva> boolean exists(Example<S> example) {
        return false;
    }
}
