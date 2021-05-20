package BarGo.Back.Repository;

import BarGo.Back.Model.Premi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiInterface extends JpaRepository<Premi, Long> {

}
