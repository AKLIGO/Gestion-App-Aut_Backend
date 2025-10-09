package inf.akligo.auth.gestionDesBiens.controllers;

import inf.akligo.auth.gestionDesBiens.entity.Vehicules;
import inf.akligo.auth.gestionDesBiens.services.serviceVehicules.ServiceVehicule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final ServiceVehicule serviceVehicule;

    // Ajouter un véhicule
    @PostMapping("/ajouter")
    public ResponseEntity<Vehicules> addVehicule(@RequestBody Vehicules vehicule) {
        Vehicules saved = serviceVehicule.addVehicules(vehicule);
        return ResponseEntity.ok(saved);
    }

    // Mettre à jour un véhicule
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Vehicules> updateVehicule(@PathVariable Long id, @RequestBody Vehicules vehicule) {
        Vehicules updated = serviceVehicule.updateVehicules(vehicule, id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Supprimer un véhicule
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> removeVehicule(@PathVariable Long id) {
        serviceVehicule.removeVehicules(id);
        return ResponseEntity.noContent().build();
    }

    // Changer le statut d'un véhicule
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Vehicules> changerStatut(@PathVariable Long id, @RequestParam String statut) {
        Vehicules updated = serviceVehicule.changerStatutVehy(statut, id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Lister tous les véhicules
    @GetMapping("/list")
    public ResponseEntity<List<Vehicules>> listVehicules() {
        return ResponseEntity.ok(serviceVehicule.listVehicules());
    }

    // Vérifier si un véhicule est disponible
    @GetMapping("/{id}/disponible")
    public ResponseEntity<Boolean> isDisponible(@PathVariable Long id) {
        return ResponseEntity.ok(serviceVehicule.isDiponible(id));
    }

    // Obtenir un véhicule par id
    @GetMapping("/{id}")
    public ResponseEntity<Vehicules> getVehiculeById(@PathVariable Long id) {
        try {
            Vehicules v = serviceVehicule.getVehiculesById(id);
            return ResponseEntity.ok(v);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
