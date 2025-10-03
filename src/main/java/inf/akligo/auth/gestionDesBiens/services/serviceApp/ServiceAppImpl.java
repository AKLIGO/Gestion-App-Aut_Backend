package inf.akligo.auth.gestionDesBiens.services.serviceApp;

import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.requests.ImageDtoApp;
import inf.akligo.auth.gestionDesBiens.requests.ImageDTO;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import org.springframework.stereotype.Service;
import inf.akligo.auth.gestionDesBiens.services.serviceApp.ServiceApp;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutAppartement;
import inf.akligo.auth.gestionDesBiens.requests.AppartementDTO;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

@Service

public class ServiceAppImpl implements ServiceApp{
    private final AppartementRepository appartementRepository;
     private static final Logger log = LoggerFactory.getLogger(ServiceAppImpl.class);
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


@Override
public List<AppartementDTO> getAllAppartementsDTO() {
    List<Appartement> appartements = appartementRepository.findAll();
    System.out.println("Nombre d'appartements récupérés : " + appartements.size());
    if (appartements == null) {
        appartements = new ArrayList<>();
    }

    return appartements.stream()
            .map(appartement -> {
                try {
                    return convertToDTO(appartement);
                } catch (Exception e) {
                    e.printStackTrace(); // log l’erreur pour chaque appartement
                    return null; // ignore l'appartement qui pose problème
                }
            })
            .filter(Objects::nonNull) // retire les DTO nuls
            .collect(Collectors.toList());
}



public AppartementDTO convertToDTO(Appartement appartement) {
    List<ImageDTO> imageDto = new ArrayList<>();

    if (appartement.getImages() != null && !appartement.getImages().isEmpty()) {
        imageDto = appartement.getImages().stream()
                .map(image -> ImageDTO.builder()
                        .id(image.getId())
                        .libelle(image.getLibelle())
                        .nomFichier(image.getNomFichier())
                        .typeMime(image.getTypeMime())
                        .appartementId(appartement.getId())
                        .previewUrl("http://localhost:8080/api/images/" + image.getId())
                        .build())
                .collect(Collectors.toList());
    }

    return AppartementDTO.builder()
            .id(appartement.getId())
            .nom(appartement.getNom())
            .adresse(appartement.getAdresse())
            .numero(appartement.getNumero())
            .superficie(appartement.getSuperficie())
            .nbrDePieces(appartement.getNbrDePieces())
            .description(appartement.getDescription())
            .prix(appartement.getPrix())
            .type(appartement.getType())
            .statut(appartement.getStatut())
            .createdAt(appartement.getCreatedAt())
            .lastModifiedDate(appartement.getLastModifiedDate())
            .images(imageDto)
            .build();
}
}