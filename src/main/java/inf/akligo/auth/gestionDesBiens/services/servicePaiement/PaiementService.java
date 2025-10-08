package inf.akligo.auth.gestionDesBiens.services.servicePaiement;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import inf.akligo.auth.gestionDesBiens.repository.ReservationRepository;
import inf.akligo.auth.gestionDesBiens.repository.PaimentRepository;
import inf.akligo.auth.gestionDesBiens.entity.Reservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.ModePaiement;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutPaiement;
import inf.akligo.auth.gestionDesBiens.entity.Paiement;
import inf.akligo.auth.gestionDesBiens.enumerateurs.StatutDeReservation;
import inf.akligo.auth.gestionDesBiens.enumerateurs.TypeDeRervation;
@Service
@RequiredArgsConstructor
public class PaiementService{
    private final ReservationRepository reservationRepository;
    private final PaimentRepository paiementRepository;

    public Paiement ajouterPaiement(Long reservartionId, double montant, ModePaiement modePaiement){

        Reservation reservation = reservationRepository.findById(reservartionId)
                    .orElseThrow( () -> new RuntimeException("Reservation non trouver"));
        
        Paiement paiement = Paiement.builder()
                .datePaiement(LocalDate.now())
                .montant(montant)
                .modePaiement(modePaiement)
                .statut(StatutPaiement.EFFECTUE)
                .reservation(reservation)
                .build();
        paiementRepository.save(paiement);

        /**
         * met a jour le statut de la reservation
         */
        double totalPaye = reservation.getMontantPaye();

        if(totalPaye >= reservation.getMontantPaye()){
            reservation.setStatut(StatutDeReservation.PAYEE);
        }
        else{
            reservation.setStatut(StatutDeReservation.PARTIELLEMENT_PAYEE);
        }

        reservationRepository.save(reservation);

        return paiement;
    

    }

    public List<Paiement> getAllPaiements(){
        return paiementRepository.findAll();
    }

}