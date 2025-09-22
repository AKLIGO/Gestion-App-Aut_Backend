package inf.akligo.auth.gestionDesBiens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import inf.akligo.auth.gestionDesBiens.entity.Immeuble;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmeubleRepository extends JpaRepository<Immeuble, Long>{

}