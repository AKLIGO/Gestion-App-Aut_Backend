package inf.akligo.auth.authConfiguration.servicesCompte;

import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import inf.akligo.auth.authConfiguration.entity.Roles;
import inf.akligo.auth.authConfiguration.repository.TokenRepository;
import inf.akligo.auth.authConfiguration.entity.Token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.time.LocalDateTime;
import java.security.SecureRandom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import inf.akligo.auth.authConfiguration.repository.UtilisateurRepository;
import inf.akligo.auth.authConfiguration.repository.RoleRepository;
import inf.akligo.auth.securityConfig.serviceEmail.EmailService;
import inf.akligo.auth.securityConfig.serviceEmail.EmailTemplateName;

@Service
@Transactional
public class ServiceCompteImpl implements ServiceCompte, UserDetailsService {

    @Value("${mailing.frontend.activation-url}")
    private String activationUrl;

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    public ServiceCompteImpl(UtilisateurRepository utilisateurRepository, 
                             RoleRepository roleRepository,
                             TokenRepository tokenRepository,
                             PasswordEncoder passwordEncoder,
                             EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Override
    public Utilisateurs ajouterUtilisateur(Utilisateurs utilisateur) {
        Roles roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER non trouvé"));

        utilisateur.setRoles(Collections.singletonList(roleUser));
        utilisateur.setEnabled(true);
        utilisateur.setAccountLocked(false);
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));

        Utilisateurs savedUser = utilisateurRepository.save(utilisateur);

        // Envoi email dans try/catch
        sendValidationEmail(savedUser);

        return savedUser;
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
    public Utilisateurs modifierUtilisateur(Utilisateurs utilisateurDetails, Long id) {
        Utilisateurs utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenoms(utilisateurDetails.getPrenoms());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        utilisateur.setTelephone(utilisateurDetails.getTelephone());
        utilisateur.setAdresse(utilisateurDetails.getAdresse());

        if (utilisateurDetails.getPassword() != null && !utilisateurDetails.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(utilisateurDetails.getPassword()));
        }

        utilisateur.setAccountLocked(utilisateurDetails.isAccountLocked());
        utilisateur.setEnabled(utilisateurDetails.isEnabled());

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void sendValidationEmail(Utilisateurs utilisateur) {
        var newToken = generateAndSaveActivationToken(utilisateur);

        try {
            emailService.sendEmail(
                    utilisateur.getEmail(),
                    utilisateur.getFullName(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    activationUrl,
                    newToken,
                    "Account activation"
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    public String generateAndSaveActivationToken(Utilisateurs utilisateur) {
        String generatedToken = generateActivationCode(6);

        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(20))
                .utilisateur(utilisateur)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    public String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
