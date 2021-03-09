package BarGo.Back.Service;

import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Rol;
import BarGo.Back.Repository.RolInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService implements RolInterface {

    @Autowired
    private RolInterface rolInterface;

    @Override
    public List<Rol> findAll() {
        return null;
    }

    @Override
    public List<Rol> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Rol> findAllById(Iterable<Long> iterable) {
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
    public void delete(Rol rol) {

    }

    @Override
    public void deleteAll(Iterable<? extends Rol> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Rol> S save(S s) {
        return rolInterface.save(s);
    }

    @Override
    public <S extends Rol> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Rol> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Rol> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Rol> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Rol getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Rol> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Rol> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Rol> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Rol> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Rol> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Rol> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Optional<Rol> findByNomRol(NomRol nomRol) {
        return rolInterface.findByNomRol(nomRol);
    }
}
