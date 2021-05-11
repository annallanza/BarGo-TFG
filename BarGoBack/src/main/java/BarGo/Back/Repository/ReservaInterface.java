package BarGo.Back.Repository;

import BarGo.Back.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaInterface extends JpaRepository<Reserva, Long> {
}
