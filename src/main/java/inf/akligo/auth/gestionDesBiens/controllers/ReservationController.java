package inf.akligo.auth.gestionDesBiens.controllers;
import inf.akligo.auth.gestionDesBiens.services.serviceReservation.ServiceReservation;
import inf.akligo.auth.gestionDesBiens.requests.ReservationRequest;
import inf.akligo.auth.gestionDesBiens.requests.ReservationResponseDTO;
import inf.akligo.auth.gestionDesBiens.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;


@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ServiceReservation reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationResponseDTO> updateReservationStatus(
            @PathVariable("id") Long reservationId,
            @RequestParam("statut") String nouveauStatut) {

        ReservationResponseDTO updatedReservation =
                reservationService.updateReservationStatus(reservationId, nouveauStatut);

        return ResponseEntity.ok(updatedReservation);
    }

    @GetMapping("/appartement/{appartementId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByAppartement(
            @PathVariable Long appartementId) {

        List<ReservationResponseDTO> reservations = reservationService.getReservationsByAppartement(appartementId);
        return ResponseEntity.ok(reservations);
    }

        // Endpoint pour récupérer la liste des réservations
    @GetMapping
    public List<ReservationResponseDTO> getReservations() {
        return reservationService.getAllReservations();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("id") Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok("Réservation supprimée avec succès !");
}


}
