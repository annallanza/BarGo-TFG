package BarGo.Back.Repository;

import BarGo.Back.Model.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariInterface extends JpaRepository<Usuari, Long> {

    Usuari findByNom(String nom);
}
