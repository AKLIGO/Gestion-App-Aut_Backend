package inf.akligo.auth.gestionDesBiens.services.serviceImage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import inf.akligo.auth.gestionDesBiens.entity.Images;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.repository.ImageRepository;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class ServiceImage {

    private final ImageRepository imagesRepository;
    private final AppartementRepository appartementsRepository;
    private static final String UPLOAD_DIR = "uploads/images/";

    public Images uploadImageWithAppartement(String libelle, MultipartFile file, Long appartementId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Appartement appartement = appartementsRepository.findById(appartementId)
                .orElseThrow(() -> new IllegalArgumentException("Appartement not found with id " + appartementId));

        // Vérifier le dossier
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Générer un nom unique
        String sanitizeFileName =sanitizeFileName(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + sanitizeFileName;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Sauvegarde physique
        Files.copy(file.getInputStream(), filePath);

        // Sauvegarde en base
        Images image = Images.builder()
                .libelle(libelle)
                .nomFichier(fileName)
                .typeMime(file.getContentType())
                .appartement(appartement)
                .build();

        return imagesRepository.save(image);
    }

    public Images uploadImageWithAppartementByName(String libelle, MultipartFile file, String nomDeAppartement) throws IOException {
        Appartement appartement = appartementsRepository.findByNom(nomDeAppartement)
                .orElseThrow(() -> new IllegalArgumentException("Appartement introuvable avec le nom " + nomDeAppartement));

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String sanitizeFileName = sanitizeFileName(file.getOriginalFilename());

        String fileName = System.currentTimeMillis() + "_" + sanitizeFileName;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Sauvegarde physique
        Files.copy(file.getInputStream(), filePath);

        // Sauvegarde en base
        Images image = Images.builder()
                .libelle(libelle)
                .nomFichier(fileName) // ✅ corrigé
                .typeMime(file.getContentType())
                .appartement(appartement)
                .build();

        return imagesRepository.save(image);
    }

    public Images getImageById(Long id) {
        return imagesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image introuvable avec l'id " + id));
    }




    // Récupération du fichier physique
    public Resource loadFileAsResource(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new IOException("Fichier introuvable: " + fileName);
        }

        return resource;
    }

    /**
     * Methode pour netoyer le nom de fichier
     */

    public String sanitizeFileName(String original){

        return original
                .replaceAll("[\\s]","_")    // remplace les espaces par _
                .replaceAll("[^a-zA-Z0-9_.-]",""); // supprime tous les caractères spéciaux sauf _ . -
    }
}
