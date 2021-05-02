package BarGo.Back.Repository;

import BarGo.Back.Model.Esdeveniment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsdevenimentInterface extends JpaRepository<Esdeveniment, Long> {

    List<Esdeveniment> findByNomContaining(String nom);
}
