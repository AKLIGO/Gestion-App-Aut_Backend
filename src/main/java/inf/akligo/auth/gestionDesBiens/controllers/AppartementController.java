package inf.akligo.auth.gestionDesBiens.controllers;

import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.services.serviceApp.ServiceApp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/appartement")
@RequiredArgsConstructor
public class AppartementController {

    private final ServiceApp serviceApp;

    @PostMapping
    public ResponseEntity<Appartement> addAppartement(@Valid @RequestBody Appartement appartement) {
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
