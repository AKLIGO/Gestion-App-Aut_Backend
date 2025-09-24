package inf.akligo.auth.gestionDesBiens.controllers;

import inf.akligo.auth.gestionDesBiens.entity.Reservation;
import inf.akligo.auth.gestionDesBiens.requests.ReservationRequestVehi;
import inf.akligo.auth.gestionDesBiens.requests.ReservationResponseVehi;
import inf.akligo.auth.gestionDesBiens.services.serviceReservation.ServiceReservation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations/vehicule")
@RequiredArgsConstructor
public class ReservationVehiculeController {

    private final ServiceReservation serviceReservation;

    // Créer une nouvelle réservation pour un véhicule
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequestVehi request) {
        Reservation reservation = serviceReservation.createReservationVehicule(request);
        return ResponseEntity.ok(reservation);
    }

    // Mettre à jour le statut d'une réservation
    @PutMapping("/{reservationId}/statut")
    public ResponseEntity<ReservationResponseVehi> updateReservationStatut(
            @PathVariable Long reservationId,
            @RequestParam String nouveauStatut
    ) {
        ReservationResponseVehi response = serviceReservation.updateReservationStatutVehi(reservationId, nouveauStatut);
        return ResponseEntity.ok(response);
    }
}
