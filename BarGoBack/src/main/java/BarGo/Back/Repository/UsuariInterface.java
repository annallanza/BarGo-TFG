package BarGo.Back.Repository;

import BarGo.Back.Model.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariInterface extends JpaRepository<Usuari, Long> {

    Optional<Usuari> findByNomUsuari(String nomUsuari);

    Optional<Usuari> findByCorreu(String correu);

    boolean existsByNomUsuari(String nomUsuari);

    boolean existsByCorreu(String correu);

}
