package inf.akligo.auth.gestionDesBiens.services.serviceReservation;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import inf.akligo.auth.gestionDesBiens.entity.Vehicules;
import java.util.Collections;
import java.util.stream.Collectors;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import inf.akligo.auth.authConfiguration.repository.UtilisateurRepository;

import inf.akligo.auth.gestionDesBiens.repository.ReservationRepository;
import inf.akligo.auth.gestionDesBiens.repository.AppartementRepository;
import inf.akligo.auth.gestionDesBiens.repository.VehiculeRepository;
import inf.akligo.auth.gestionDesBiens.entity.Appartement;
import inf.akligo.auth.gestionDesBiens.entity.Reservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutDeReservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.TypeDeRervation;
import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import java.io.IOException;
import inf.akligo.auth.gestionDesBiens.requests.ReservationRequest;
import inf.akligo.auth.gestionDesBiens.requests.ReservationResponseDTO;

import inf.akligo.auth.gestionDesBiens.requests.ReservationRequestVehi;
import inf.akligo.auth.gestionDesBiens.requests.ReservationResponseVehi;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ServiceReservation{

    private final ReservationRepository reservationRepository;
    private final AppartementRepository appartementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;


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

    @Transactional
public ReservationResponseDTO updateReservationStatus(Long reservationId, String nouveauStatutStr) {
    // Récupérer la réservation
    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

    // Récupérer l'utilisateur lié à la réservation
    Utilisateurs utilisateur = reservation.getUtilisateur();

    // Convertir le String en enum
    StatutDeReservation nouveauStatut;
    try {
        nouveauStatut = StatutDeReservation.valueOf(nouveauStatutStr.toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Statut invalide");
    }

    // Mettre à jour le statut
    reservation.setStatut(nouveauStatut);
    reservationRepository.save(reservation);

    // Retourner le DTO
    return ReservationResponseDTO.builder()
            .id(reservation.getId())
            .dateDebut(reservation.getDateDebut())
            .dateFin(reservation.getDateFin())
            .appartementNom(reservation.getAppartement().getNom())
            .appartementAdresse(reservation.getAppartement().getAdresse())
            .utilisateurNom(utilisateur.getNom())
            .utilisateurPrenoms(utilisateur.getPrenoms())
            .statut(reservation.getStatut().name())
            .build();
        }


@Transactional
public Reservation createReservationVehicule(ReservationRequestVehi request) {
    // Récupérer le username depuis le token via SecurityContextHolder
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    // Récupérer l'utilisateur connecté
    Utilisateurs utilisateur = utilisateurRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    // Récupérer le véhicule
    Vehicules vehicule = vehiculeRepository.findById(request.getVehiculeId())
            .orElseThrow(() -> new RuntimeException("Véhicule introuvable"));

    // Récupérer les réservations confirmées pour ce véhicule
    List<Reservation> reservations = reservationRepository
            .findByVehiculeAndStatut(vehicule, StatutDeReservation.CONFIRMEE)
            .orElse(Collections.emptyList());

    // Vérifier si le véhicule est déjà réservé aux dates demandées
    boolean dejaReserve = reservations.stream()
            .anyMatch(r ->
                    !(request.getDateFin().isBefore(r.getDateDebut()) ||
                      request.getDateDebut().isAfter(r.getDateFin()))
            );

    if (dejaReserve) {
        throw new RuntimeException("Véhicule déjà réservé à ces dates !");
    }

    // Créer la réservation
    Reservation reservation = Reservation.builder()
            .dateDebut(request.getDateDebut())
            .dateFin(request.getDateFin())
            .type(TypeDeRervation.VEHICULE)
            .statut(StatutDeReservation.EN_ATTENTE)
            .vehicule(vehicule)
            .utilisateur(utilisateur)
            .build();

    return reservationRepository.save(reservation);
}

@Transactional
public ReservationResponseVehi updateReservationStatutVehi(Long reservartionId, String nouveauStatut){
        // Recuperer la reservation
        Reservation reservation = reservationRepository.findById(reservartionId)
                .orElseThrow(() -> new RuntimeException("Reservation non trouver"));

                    // Récupérer l'utilisateur lié à la réservation
        Utilisateurs utilisateur = reservation.getUtilisateur();

        // Convertir le String en enum

        StatutDeReservation nouveau;
        try{
                nouveau= StatutDeReservation.valueOf(nouveauStatut.toUpperCase());

        } catch (IllegalArgumentException e) {
                throw new RuntimeException("Statut invalide");
    }
        // mettre a jour le statut

        reservation.setStatut(nouveau);
        reservationRepository.save(reservation);

        // retourner le DTO

        return ReservationResponseVehi.builder()
                        .id(reservation.getId())
                        .dateDebut(reservation.getDateDebut())
                        .dateFin(reservation.getDateFin())
                        .vehiculeMarque(reservation.getVehicule().getMarque())
                        .vehiculeImmatriculation(reservation.getVehicule().getImmatriculation())
                        .utilisateurNom(utilisateur.getNom())
                        .utilisateurPrenoms(utilisateur.getPrenoms())
                        .statut(reservation.getStatut().name())
                        .build();
                        
        }



        public List<ReservationResponseDTO> getReservationsByAppartement(Long appartementId) {
        return reservationRepository.findByAppartementId(appartementId)
                .stream()
                .map(reservation -> new ReservationResponseDTO(
                        reservation.getId(),
                        reservation.getDateDebut(),
                        reservation.getDateFin(),
                        reservation.getAppartement() != null ? reservation.getAppartement().getNom() : null,
                        reservation.getAppartement() != null ? reservation.getAppartement().getAdresse() : null,
                        reservation.getUtilisateur() != null ? reservation.getUtilisateur().getNom() : null,
                        reservation.getUtilisateur() != null ? reservation.getUtilisateur().getPrenoms() : null,
                        reservation.getStatut() != null ? reservation.getStatut().name() : null
                ))
                .collect(Collectors.toList());
    }



        /**
         * Methode pour recuperer toutes les reservations
         */

       public List<ReservationResponseDTO> getAllReservations() {
    List<Reservation> reservations = reservationRepository.findAll();

    return reservations.stream()
            .map(r -> new ReservationResponseDTO(
                    r.getId(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getAppartement() != null ? r.getAppartement().getNom() : null,
                    r.getAppartement() != null ? r.getAppartement().getAdresse() : null,
                    r.getUtilisateur() != null ? r.getUtilisateur().getNom() : null,
                    r.getUtilisateur() != null ? r.getUtilisateur().getPrenoms() : null,
                    r.getStatut() != null ? r.getStatut().name() : null
            ))
            .collect(Collectors.toList());
        }


        @Transactional
        public void deleteReservation(Long reservationId) {
    // Vérifier si la réservation existe
                Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + reservationId));

    // Supprimer la réservation
                reservationRepository.delete(reservation);
}


}