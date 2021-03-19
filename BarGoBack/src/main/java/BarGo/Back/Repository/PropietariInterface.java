package BarGo.Back.Repository;

import BarGo.Back.Model.Propietari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropietariInterface extends JpaRepository<Propietari, Long> {
}
