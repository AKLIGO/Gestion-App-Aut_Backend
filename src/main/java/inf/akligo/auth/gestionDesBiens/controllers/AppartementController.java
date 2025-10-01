package inf.akligo.auth.gestionDesBiens.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.services.serviceApp.ServiceApp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/appartement")

@RequiredArgsConstructor
public class AppartementController {

    private final ServiceApp serviceApp;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Appartement> addAppartement(@Valid @RequestBody Appartement appartement) {
        System.out.println("Tentative d'ajout d'un appartement par l'utilisateur : " + SecurityContextHolder.getContext().getAuthentication().getName());
        Appartement appartementSave = serviceApp.addAppartement(appartement);
        return ResponseEntity.ok(appartementSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appartement> modifierApp(@Valid @RequestBody Appartement appartement, @PathVariable Long id) {
        Appartement updatedApp = serviceApp.updateAppartement(appartement, id);
        return ResponseEntity.ok(updatedApp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerApp(@PathVariable Long id) {
        serviceApp.removeAppartement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Appartement>> getAllAppartements() {
        return ResponseEntity.ok(serviceApp.listAppartement());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appartement> getAppartementById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceApp.getAppartementById(id));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Appartement> changerStatut(@PathVariable Long id, @RequestParam String statut) {
        Appartement updated = serviceApp.changerStatutApp(statut, id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/disponible")
    public ResponseEntity<Boolean> isDisponible(@PathVariable Long id) {
        boolean disponible = serviceApp.isDiponible(id);
        return ResponseEntity.ok(disponible);
    }


}
