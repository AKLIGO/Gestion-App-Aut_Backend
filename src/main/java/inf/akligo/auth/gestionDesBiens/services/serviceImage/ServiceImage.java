package inf.akligo.auth.gestionDesBiens.services.serviceImage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import inf.akligo.auth.gestionDesBiens.entity.Images;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import org.springframework.stereotype.Service;
import inf.akligo.auth.gestionDesBiens.repository.ImageRepository;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ServiceImage{

    private final ImageRepository imagesRepository;
    private final AppartementRepository appartementsRepository;

        public Images uploadImageWithAppartement(String libelle, MultipartFile file, Long appartementId) throws IOException {
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }

        if(imagesRepository.existsByLibelle(libelle)){
            throw new IllegalArgumentException("Image with this libelle already exists");
        }

        Appartement appartement = appartementsRepository.findById(appartementId)
                .orElseThrow(() -> new IllegalArgumentException("Appartement not found with id " + appartementId));

        Images image = Images.builder()
                .libelle(libelle)
                .photo(file.getBytes())
                .nomFichier(file.getOriginalFilename())
                .typeMime(file.getContentType())
                .appartement(appartement)
                .build();

        return imagesRepository.save(image);
    }


    public Images uploadImageWithAppartementByName(String libelle,MultipartFile file, String nomDeAppartement) throws IOException {
        Appartement appartement = appartementsRepository.findByNom(nomDeAppartement)
                .orElseThrow(() -> new IllegalArgumentException("Appartement introuvable avec le nom " + nomDeAppartement));
        
        Images image = Images.builder()
                .libelle(libelle)
                .photo(file.getBytes())
                .nomFichier(file.getOriginalFilename())
                .typeMime(file.getContentType())
                .appartement(appartement)
                .build();

        return imagesRepository.save(image);

    }

        public Images getImageById(Long id) {
            return imagesRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Image introuvable avec l'id " + id));
    }

}