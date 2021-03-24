package BarGo.Back.Repository;

import BarGo.Back.Model.Establiment;
import BarGo.Back.Model.Propietari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropietariInterface extends JpaRepository<Propietari, Long> {

    @Query("SELECT p.establiment FROM Propietari p WHERE p.id = :id")
    Optional<Establiment> getEstablimentByUsuariId(@Param("id") Long id);
}
