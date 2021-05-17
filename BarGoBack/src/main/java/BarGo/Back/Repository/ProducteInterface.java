package BarGo.Back.Repository;

import BarGo.Back.Model.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducteInterface extends JpaRepository<Producte, Long> {

    Optional<Producte> findByCodi(String codi);

    boolean existsByCodi(String codi);
}
