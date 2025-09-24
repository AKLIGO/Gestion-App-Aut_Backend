package inf.akligo.auth.gestionDesBiens.controllers;
import inf.akligo.auth.gestionDesBiens.entity.Immeuble;

import inf.akligo.auth.gestionDesBiens.services.serviceCompteImpl.ServiceImmeuble;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/immeubles")
@RequiredArgsConstructor

public class ImmeubleControllers{
    private final ServiceImmeuble serviceImmeuble;

    @PostMapping
    public ResponseEntity<Immeuble> addImmeuble(@Valid @RequestBody Immeuble immeuble){
        Immeuble saved = serviceImmeuble.addImmeuble(immeuble);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Immeuble> updateImmeuble(@PathVariable Long id, @RequestBody
                        Immeuble immeubleUpdate){
        Immeuble update = serviceImmeuble.updateImmeuble(immeubleUpdate,id);
        return ResponseEntity.ok(update);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> removeImmeuble(@PathVariable Long id){
        serviceImmeuble.removeImmeuble(id);
        return ResponseEntity.noContent().build();
     }

     @GetMapping
     public ResponseEntity<List<Immeuble>> listImmeuble(){
        return ResponseEntity.ok(serviceImmeuble.listImmeuble());
     }

}