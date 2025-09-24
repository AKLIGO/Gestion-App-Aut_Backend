package inf.akligo.auth.gestionDesBiens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import inf.akligo.auth.gestionDesBiens.entity.Vehicules;
import inf.akligo.auth.gestionDesBiens.enumerateurs.TypeVehicule;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutVehicule;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicules, Long>{

    Optional<Vehicules> findByIdAndStatut(Long id, StatutVehicule statut);
    Optional<Vehicules> findByMarque(String marque);

}