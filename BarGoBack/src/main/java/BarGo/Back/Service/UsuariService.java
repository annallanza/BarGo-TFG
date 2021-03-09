package BarGo.Back.Service;

import BarGo.Back.Model.Usuari;
import BarGo.Back.Repository.UsuariInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional //Per mantenir la coherencia a la base de dades, per quan hi ha dos accessos simultanis
public class UsuariService implements UsuariInterface {

    @Autowired //Serveix per evitar posar new UsuariInterface...
    private UsuariInterface usuariInterface;

    @Override
    public List<Usuari> findAll() {
        return usuariInterface.findAll();
    }

    @Override
    public List<Usuari> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Usuari> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Usuari> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        usuariInterface.deleteById(aLong);
    }

    @Override
    public void delete(Usuari usuari) {
        usuariInterface.delete(usuari);
    }

    @Override
    public void deleteAll(Iterable<? extends Usuari> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Usuari> S save(S s) {
        return usuariInterface.save(s);
    }

    @Override
    public <S extends Usuari> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Usuari> findById(Long aLong) {
        return usuariInterface.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return usuariInterface.existsById(aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Usuari> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Usuari> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Usuari getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Usuari> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Usuari> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Usuari> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Usuari> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Usuari> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Usuari> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Optional<Usuari> findByNomUsuari(String nomUsuari) {
        return usuariInterface.findByNomUsuari(nomUsuari);
    }

    @Override
    public boolean existsByNomUsuari(String nomUsuari) {
        return usuariInterface.existsByNomUsuari(nomUsuari);
    }
}
