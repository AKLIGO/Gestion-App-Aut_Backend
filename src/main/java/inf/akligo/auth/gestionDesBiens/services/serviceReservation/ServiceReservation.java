package inf.akligo.auth.gestionDesBiens.services.serviceReservation;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import inf.akligo.auth.authConfiguration.repository.UtilisateurRepository;

import inf.akligo.auth.gestionDesBiens.repository.ReservationRepository;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.entity.Reservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutDeReservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.TypeDeRervation;
import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import java.io.IOException;
import inf.akligo.auth.gestionDesBiens.requests.ReservationRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ServiceReservation{

    private final ReservationRepository reservationRepository;
    private final AppartementRepository appartementRepository;
    private final UtilisateurRepository utilisateurRepository;


    @Transactional
    public Reservation createReservation(ReservationRequest request) {
        // Récupérer le username depuis le token via SecurityContextHolder

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Récupérer l'utilisateur connecté
        Utilisateurs utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l’appartement existe
        Appartement appartement = appartementRepository.findById(request.getAppartementId())
                .orElseThrow(() -> new RuntimeException("Appartement introuvable"));

        // Vérifier si déjà réservé
        boolean dejaReserve = reservationRepository
                .findByAppartementAndStatut(appartement, StatutDeReservation.CONFIRMEE)
                .stream()
                .anyMatch(r ->
                        !(request.getDateFin().isBefore(r.getDateDebut()) ||
                          request.getDateDebut().isAfter(r.getDateFin()))
                );

        if (dejaReserve) {
            throw new RuntimeException("Appartement déjà réservé à ces dates !");
        }

        // Créer la réservation avec valeurs par défaut
        Reservation reservation = Reservation.builder()
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .type(TypeDeRervation.APPARTEMENT)
                .statut(StatutDeReservation.EN_ATTENTE) 
                .appartement(appartement)
                .utilisateur(utilisateur)
                .build();

        return reservationRepository.save(reservation);
    }



}