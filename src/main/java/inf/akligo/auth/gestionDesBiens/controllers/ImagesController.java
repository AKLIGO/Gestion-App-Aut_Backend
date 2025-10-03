package inf.akligo.auth.gestionDesBiens.controllers;
import inf.akligo.auth.gestionDesBiens.requests.ImageDtoApp;
import inf.akligo.auth.gestionDesBiens.requests.ImageDTO;
import inf.akligo.auth.gestionDesBiens.entity.Images;
import inf.akligo.auth.gestionDesBiens.services.serviceImage.ServiceImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;



import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImagesController{

    private final ServiceImage serviceImage;

    /**
     * Uploader une image et la lier à un appartement
     */
    @PostMapping("/ajoutImageApp")
    public ResponseEntity<?> uploadImageWithAppartement(
            @RequestParam("libelle") String libelle,
            @RequestParam("file") MultipartFile file,
            @RequestParam("appartementId") Long appartementId) {
        try {
            Images image = serviceImage.uploadImageWithAppartement(libelle, file, appartementId);
            return ResponseEntity.ok(image);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'upload: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @PostMapping("/ajoutImaAppNom")
    public ResponseEntity<?> uploadImageWithAppartementByName(
            @RequestParam("libelle") String libelle,
            @RequestParam("file") MultipartFile file,
            @RequestParam("appartementNom") String appartementNom
    ){
        try{
            Images image =serviceImage.uploadImageWithAppartementByName(libelle, file, appartementNom);
            return ResponseEntity.ok(image);
        }catch (IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'upload: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Retourne juste les infos en base 
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        try {
            Images image = serviceImage.getImageById(id);
            return ResponseEntity.ok(new ImageDtoApp(image.getId(),
                                    image.getLibelle(),
                                    image.getNomFichier(),
                                    image.getTypeMime(),
                                    image.getAppartement() != null ? image.getAppartement().getId() : null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint pour servir le fichier physique
     */

    @GetMapping("file/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        try {
            
            Resource resource = serviceImage.loadFileAsResource(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}