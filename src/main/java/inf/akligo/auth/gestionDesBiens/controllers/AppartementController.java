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

}
