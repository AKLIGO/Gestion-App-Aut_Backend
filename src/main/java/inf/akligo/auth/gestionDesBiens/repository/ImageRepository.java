package inf.akligo.auth.gestionDesBiens.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import inf.akligo.auth.gestionDesBiens.entity.Images;


@Repository
public interface ImageRepository extends JpaRepository<Images, Long>{
    // Méthode pour rechercher par libellé
    Images findByLibelle(String libelle);
    
    // Méthode pour vérifier si une image existe par libellé
    boolean existsByLibelle(String libelle);
    void deleteByLibelle(String libelle);
}