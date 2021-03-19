package BarGo.Back.Repository;

import BarGo.Back.Model.Consumidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumidorInterface extends JpaRepository<Consumidor, Long> {
}
