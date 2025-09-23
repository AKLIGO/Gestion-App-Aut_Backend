package inf.akligo.auth.gestionDesBiens.services.serviceApp;

import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import org.springframework.stereotype.Service;
import inf.akligo.auth.gestionDesBiens.services.serviceApp.ServiceApp;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutAppartement;


import java.util.List;
import java.util.Optional;

@Service

public class ServiceAppImpl implements ServiceApp{
    private final AppartementRepository appartementRepository;

    public ServiceAppImpl(AppartementRepository appartementRepository) {
        this.appartementRepository = appartementRepository;
    }

    @Override
    public Appartement addAppartement(Appartement appartement){
        return appartementRepository.save(appartement);
    }

    @Override
    public Appartement updateAppartement(Appartement appartementUpd, Long id){
        Optional<Appartement> optAppartement = appartementRepository.findById(id);
        if (optAppartement.isPresent()){
            Appartement appartement=optAppartement.get();
            appartement.setNom(appartementUpd.getNom());
            appartement.setNumero(appartementUpd.getNumero());
            appartement.setSuperficie(appartementUpd.getSuperficie());
            appartement.setAdresse(appartementUpd.getAdresse());
            appartement.setNbrDePieces(appartementUpd.getNbrDePieces());
            appartement.setDescription(appartementUpd.getDescription());
            appartement.setStatut(appartementUpd.getStatut());
            appartement.setType(appartementUpd.getType());

            return appartementRepository.save(appartement);

        }
        else {
            throw new RuntimeException("Appartement avec l'id " + id + " non trouvé");
        }
    }

    @Override
    public void removeAppartement(Long id) {
        appartementRepository.deleteById(id);
    }



    @Override
    public List<Appartement> listAppartement() {
        return appartementRepository.findAll();
    }

    @Override
    public Appartement changerStatutApp(String nvStatut,Long id){
        //recuperer l'appartement dans la Base
        Appartement optApp = appartementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Appartement nn trouver"));

         // Convertir String en enum
        StatutAppartement nvStatutConv = StatutAppartement.valueOf(nvStatut);

        // affecter une nouvelle valeur (Statut) a l'appartement
        
        optApp.setStatut(nvStatutConv);

        // enregistrer l'appartement apres avoir effectuer la modification
        return appartementRepository.save(optApp);
    }

    @Override
    public boolean isDiponible(Long appartementId){

        //recuperer l'appartement
        // Appartement appDisp= appartementRepository.findById(appartementId);

        // retourner vrai si le statut de l'appartement rechercher est dispo ; false dans le cas contraire

        return appartementRepository.findByIdAndStatut(appartementId,StatutAppartement.DISPONIBLE)
                    .isPresent();

    };


    
    @Override
    public Appartement getAppartementById(Long id){
        // recuperer un utilisateur a partir de son identifiant
        return appartementRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("appartement non trouver"));
    }

}