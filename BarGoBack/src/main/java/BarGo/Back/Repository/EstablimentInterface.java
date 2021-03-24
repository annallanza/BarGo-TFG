package BarGo.Back.Repository;

import BarGo.Back.Model.Establiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablimentInterface extends JpaRepository<Establiment, Long> {

}
