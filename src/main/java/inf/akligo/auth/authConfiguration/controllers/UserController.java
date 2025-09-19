package inf.akligo.auth.authConfiguration.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import inf.akligo.auth.authConfiguration.entity.Utilisateurs;
import inf.akligo.auth.authConfiguration.entity.Roles;
import inf.akligo.auth.authConfiguration.entity.Token;
import inf.akligo.auth.authConfiguration.repository.TokenRepository;
import inf.akligo.auth.authConfiguration.request.AuthenticationRequest;
import inf.akligo.auth.authConfiguration.request.AuthenticationResponse;

import inf.akligo.auth.authConfiguration.servicesCompte.ServiceCompteImpl;
import inf.akligo.auth.authConfiguration.datas.RoleUser;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("auth")
@RestController

public class UserController{

    private ServiceCompteImpl serviceCompteImpl;
    //private final TokenRepository tokenRepository;

    public UserController(ServiceCompteImpl serviceCompteImpl){
        this.serviceCompteImpl=serviceCompteImpl;
    }
    @GetMapping(path="/listUser")
    public List<Utilisateurs> listeUtilisateur(){

        return serviceCompteImpl.listeUtilisateur();

    }

    @PostMapping(path="/addUser")
    public Utilisateurs ajouterUtilisateur(@RequestBody Utilisateurs utilisateur){
        return serviceCompteImpl.ajouterUtilisateur(utilisateur);
    }

    @PostMapping(path="/addRole")

    public Roles ajouterRole(@RequestBody Roles role){
        return serviceCompteImpl.ajouterRole(role);
    }

    @PostMapping(path="/addRoleAUtilisateur")
    public void ajouterRoleAUtilisateur(@RequestBody RoleUser roleUser){
        serviceCompteImpl.ajouterRoleAUtilisateur(roleUser.getEmail(),roleUser.getName());
    }

    @DeleteMapping(path="/supprimerUtilisateur/{id}")

    public void supprimerUtilisateur(@PathVariable Long id){
        serviceCompteImpl.supprimerUtilisateurParId(id);
    }

    @PutMapping(path="/modifierUtilisateur/{id}")
    public Utilisateurs modifierUtilisateur( @RequestBody Utilisateurs utilisateurDetails,
    @PathVariable Long id){
        return serviceCompteImpl.modifierUtilisateur(utilisateurDetails,id);
    }

    @PostMapping(path="/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody @Valid AuthenticationRequest request
    ){

        return ResponseEntity.ok(serviceCompteImpl.authenticate(request));
    }


    @PostMapping("/activation-account")
    public ResponseEntity<?> confirm(
        @RequestParam String token
    ) {
        try {
            serviceCompteImpl.activationAccount(token);
            return ResponseEntity.ok("Compte activé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

   


}