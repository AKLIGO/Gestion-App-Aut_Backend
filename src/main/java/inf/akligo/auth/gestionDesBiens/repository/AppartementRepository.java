package inf.akligo.auth.gestionDesBiens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutAppartement;

@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long>{

    Optional<Appartement> findByIdAndStatut(Long id, StatutAppartement statut);

}