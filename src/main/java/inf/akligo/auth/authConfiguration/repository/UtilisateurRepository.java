package inf.akligo.auth.authConfiguration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateurs,Long>{

    Optional<Utilisateurs> findByEmail(String email);
}