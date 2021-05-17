package BarGo.Back.Repository;

import BarGo.Back.Model.Establiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablimentInterface extends JpaRepository<Establiment, Long> {

    List<Establiment> findByNomContaining(String nom);

    List<Establiment> findByDireccioContaining(String direccio);

    Optional<Establiment> findByCodi(String codi);

    boolean existsByCodi(String codi);
}
