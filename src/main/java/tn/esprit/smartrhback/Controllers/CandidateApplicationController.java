package tn.esprit.smartrhback.Controllers;


import tn.esprit.smartrhback.Controllers.dto.CandidateApplicationResponse;
import tn.esprit.smartrhback.Controllers.dto.CreateCandidateApplicationRequest;
import tn.esprit.smartrhback.entities.Decision;
import tn.esprit.smartrhback.services.CandidateApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class CandidateApplicationController {

    @Autowired
    private CandidateApplicationService service;

    @PostMapping
    public ResponseEntity<CandidateApplicationResponse> create(@RequestBody CreateCandidateApplicationRequest request) {
        return ResponseEntity.ok(service.createApplication(request.getCvId(), request.getDemandeId()));
    }

    @GetMapping("/demande/{demandeId}")
    public ResponseEntity<List<CandidateApplicationResponse>> getByDemande(@PathVariable Long demandeId) {
        return ResponseEntity.ok(service.getByDemande(demandeId));
    }

    @GetMapping("/cv/{cvId}")
    public ResponseEntity<List<CandidateApplicationResponse>> getByCv(@PathVariable Long cvId) {
        return ResponseEntity.ok(service.getByCv(cvId));
    }

    @PatchMapping("/{id}/decision")
    public ResponseEntity<CandidateApplicationResponse> updateDecision(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Decision decision = Decision.valueOf(body.get("decision"));
        String comment = body.getOrDefault("comment", "");
        return ResponseEntity.ok(service.updateDecision(id, decision, comment));
    }
}