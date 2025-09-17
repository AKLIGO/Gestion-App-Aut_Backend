package inf.akligo.auth.authConfiguration.servicesCompte;

import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import inf.akligo.auth.authConfiguration.entity.Roles;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Collections;

import inf.akligo.auth.authConfiguration.repository.UtilisateurRepository;
import inf.akligo.auth.authConfiguration.repository.RoleRepository;

@Service
@Transactional
public class ServiceCompteImpl implements ServiceCompte {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    public ServiceCompteImpl(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Utilisateurs ajouterUtilisateur(Utilisateurs utilisateur) {
        // Récupérer le rôle par défaut "USER"
        Roles roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER non trouvé"));

        // Attribuer le rôle par défaut à l'utilisateur
        utilisateur.setRoles(Collections.singletonList(roleUser));

        // Sauvegarder l'utilisateur
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Roles ajouterRole(Roles role) {
        return roleRepository.save(role);
    }

    @Override
    public void ajouterRoleAUtilisateur(String email, String name) {
        Utilisateurs utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Roles role = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role non trouvé"));

        utilisateur.getRoles().add(role);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateurs loadUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public List<Utilisateurs> listeUtilisateur() {
        return utilisateurRepository.findAll();
    }


    @Override
    public void supprimerUtilisateurParId(Long id) {
    Utilisateurs utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

    utilisateurRepository.delete(utilisateur);
    }

    @Override
    public Utilisateurs modifierUtilisateur(Utilisateurs utilisateurDetails, Long id){
        Utilisateurs utilisateur= utilisateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("utilisateur non trouver"));

        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenoms(utilisateurDetails.getPrenoms());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        utilisateur.setTelephone(utilisateurDetails.getTelephone());
        utilisateur.setAdresse(utilisateurDetails.getAdresse());
        utilisateur.setPassword(utilisateurDetails.getPassword());
        utilisateur.setAccountLocked(utilisateurDetails.isAccountLocked());
        utilisateur.setEnabled(utilisateurDetails.isEnabled());

        return utilisateurRepository.save(utilisateur);
    };

}
