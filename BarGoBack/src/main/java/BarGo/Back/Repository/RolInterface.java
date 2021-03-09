package BarGo.Back.Repository;

import BarGo.Back.Enums.NomRol;
import BarGo.Back.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolInterface extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNomRol(NomRol nomRol);

}
