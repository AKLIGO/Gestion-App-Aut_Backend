package inf.akligo.auth.gestionDesBiens.services.serviceVehicules;

import inf.akligo.auth.gestionDesBiens.entity.Vehicules;
import inf.akligo.auth.gestionDesBiens.repository.VehiculeRepository;
import org.springframework.stereotype.Service;
import inf.akligo.auth.gestionDesBiens.services.serviceVehicules.ServiceVehicule;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutVehicule;
import inf.akligo.auth.gestionDesBiens.enumerateurs.TypeVehicule;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceVehiculeIm implements ServiceVehicule{

    private final VehiculeRepository vehiculeRepository;

    @Override
    public Vehicules addVehicules(Vehicules vehicules){
        return vehiculeRepository.save(vehicules);
    }
    @Override
    public Vehicules updateVehicules(Vehicules vehiculesUpd,Long id){

        // 1. Récupérer le véhicule existant
    Optional<Vehicules> optionalVehicule = vehiculeRepository.findById(id);
    
    if (optionalVehicule.isPresent()) {
        Vehicules vehicule = optionalVehicule.get();

        // 2. Mettre à jour les champs
        if (vehiculesUpd.getMarque() != null) vehicule.setMarque(vehiculesUpd.getMarque());
        if (vehiculesUpd.getModele() != null) vehicule.setModele(vehiculesUpd.getModele());
        if (vehiculesUpd.getImmatriculation() != null) vehicule.setImmatriculation(vehiculesUpd.getImmatriculation());
        if (vehiculesUpd.getPrix() != 0) vehicule.setPrix(vehiculesUpd.getPrix());
        if (vehiculesUpd.getCarburant() != null) vehicule.setCarburant(vehiculesUpd.getCarburant());
        if (vehiculesUpd.getStatut() != null) vehicule.setStatut(vehiculesUpd.getStatut());
        if (vehiculesUpd.getType() != null) vehicule.setType(vehiculesUpd.getType());
        // Pour les images, il faudra gérer la mise à jour séparément si nécessaire

        // 3. Sauvegarder le véhicule
        return vehiculeRepository.save(vehicule);
    } else {
        // Si le véhicule n'existe pas, retourner null ou lancer une exception
        return null;
    }

       
    }

    @Override
    public void removeVehicules(Long id){

        vehiculeRepository.deleteById(id);

    }

    @Override
    public Vehicules changerStatutVehy(String nvStatut,Long id){
         Optional<Vehicules> v = vehiculeRepository.findById(id);
        if (v.isPresent()) {
            // Conversion de String vers Enum
            try {
                StatutVehicule statutEnum = StatutVehicule.valueOf(nvStatut.toUpperCase());
                v.get().setStatut(statutEnum);
                return vehiculeRepository.save(v.get());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Statut invalide : " + nvStatut);
             }
        }
        return null;
     }
    @Override
    public List<Vehicules> listVehicules(){

        return vehiculeRepository.findAll();
    }
    @Override
    public boolean isDiponible(Long vehiculesId){
        return vehiculeRepository.findById(vehiculesId)
            .map(v -> v.getStatut() == StatutVehicule.DISPONIBLE)
            .orElse(false);
    }
    @Override
    public Vehicules getVehiculesById(Long id){

        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicule non trouvé avec id: " + id));

    }


}