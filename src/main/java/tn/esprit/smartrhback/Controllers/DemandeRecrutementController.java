package tn.esprit.smartrhback.Controllers;

import tn.esprit.smartrhback.Controllers.dto.DemandeRequest;
import tn.esprit.smartrhback.Controllers.dto.DemandeResponse;
import tn.esprit.smartrhback.entities.DemandeRecrutement;
import tn.esprit.smartrhback.entities.StatutDemande;
import tn.esprit.smartrhback.entities.User;
import tn.esprit.smartrhback.Repositories.DemandeRecrutementRepository;
import tn.esprit.smartrhback.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
public class DemandeRecrutementController {

    @Autowired
    private DemandeRecrutementRepository demandeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<DemandeResponse>> getAll() {
        List<DemandeResponse> demandes = demandeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeResponse> getById(@PathVariable Long id) {
        return demandeRepository.findById(id)
                .map(d -> ResponseEntity.ok(toResponse(d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DemandeResponse> create(@RequestBody DemandeRequest request, Authentication authentication) {
        User createur = userRepository.findByEmail(authentication.getName()).orElseThrow();

        DemandeRecrutement demande = new DemandeRecrutement();
        demande.setTitre(request.getTitre());
        demande.setDescription(request.getDescription());
        demande.setCompetencesRequises(request.getCompetencesRequises());
        demande.setCompetencesSouhaitees(request.getCompetencesSouhaitees());
        demande.setNiveauExperience(request.getNiveauExperience());
        demande.setDateBesoin(request.getDateBesoin());
        demande.setUrgence(request.isUrgence());
        demande.setCreateur(createur);

        if (request.getDemandeurId() != null) {
            User demandeur = userRepository.findById(request.getDemandeurId()).orElseThrow();
            demande.setDemandeur(demandeur);
        } else {
            demande.setDemandeur(createur);
        }

        demandeRepository.save(demande);
        return ResponseEntity.ok(toResponse(demande));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemandeResponse> update(@PathVariable Long id, @RequestBody DemandeRequest request) {
        DemandeRecrutement demande = demandeRepository.findById(id).orElseThrow();

        demande.setTitre(request.getTitre());
        demande.setDescription(request.getDescription());
        demande.setCompetencesRequises(request.getCompetencesRequises());
        demande.setCompetencesSouhaitees(request.getCompetencesSouhaitees());
        demande.setNiveauExperience(request.getNiveauExperience());
        demande.setDateBesoin(request.getDateBesoin());
        demande.setUrgence(request.isUrgence());

        demandeRepository.save(demande);
        return ResponseEntity.ok(toResponse(demande));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<DemandeResponse> updateStatut(@PathVariable Long id, @RequestBody StatutDemande statut) {
        DemandeRecrutement demande = demandeRepository.findById(id).orElseThrow();
        demande.setStatut(statut);
        demandeRepository.save(demande);
        return ResponseEntity.ok(toResponse(demande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        demandeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private DemandeResponse toResponse(DemandeRecrutement d) {
        return new DemandeResponse(
                d.getId(),
                d.getTitre(),
                d.getDescription(),
                d.getCompetencesRequises(),
                d.getCompetencesSouhaitees(),
                d.getNiveauExperience(),
                d.getDateBesoin(),
                d.isUrgence(),
                d.getStatut(),
                d.getDemandeur() != null ? d.getDemandeur().getFirstName() + " " + d.getDemandeur().getLastName() : null,
                d.getCreateur() != null ? d.getCreateur().getFirstName() + " " + d.getCreateur().getLastName() : null,
                d.getDateCreation()
        );
    }
}