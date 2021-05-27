package BarGo.Back.Repository;

import BarGo.Back.Model.Repte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepteInterface extends JpaRepository<Repte, Long> {

}
