package tn.esprit.smartrhback.Controllers;

import tn.esprit.smartrhback.Controllers.dto.CvUploadResponse;
import tn.esprit.smartrhback.entities.*;
import tn.esprit.smartrhback.Repositories.CvRepository;
import tn.esprit.smartrhback.Repositories.DemandeRecrutementRepository;
import tn.esprit.smartrhback.Repositories.UserRepository;
import tn.esprit.smartrhback.services.CvParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    @Autowired private CvRepository cvRepository;
    @Autowired private DemandeRecrutementRepository demandeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CvParsingService parsingService;
    @Autowired private tn.esprit.smartrhback.services.CandidateApplicationService candidateApplicationService;

    private final String UPLOAD_DIR = "uploads/cvs/";

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("source") SourceCondidat source,
            @RequestParam(value = "demandeId", required = false) Long demandeId,
            @RequestParam(value = "commentaire", required = false) String commentaire,
            Authentication authentication
    ) {
        try {
            User uploader = userRepository.findByEmail(authentication.getName()).orElseThrow();

            // Stockage physique du fichier
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String nomUnique = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destination = Paths.get(UPLOAD_DIR + nomUnique);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Extraction
            String texte = parsingService.extractText(file);
            String email = parsingService.extractEmail(texte);
            String telephone = parsingService.extractTelephone(texte);
            String nom = parsingService.extractNom(texte, file.getOriginalFilename());
            String experiences = parsingService.extractSection(texte, "expérience", "experience");
            String formation = parsingService.extractSection(texte, "formation", "diplôme", "education");

            // Détection de doublon simple (par email)
            boolean doublon = email != null && cvRepository.findByEmailCandidat(email).isPresent();

            Cv cv = new Cv();
            cv.setFichierPath(destination.toString());
            cv.setNomFichierOriginal(file.getOriginalFilename());
            cv.setTexteExtrait(texte.length() > 9900 ? texte.substring(0, 9900) : texte);
            cv.setNomCandidat(nom);
            cv.setEmailCandidat(email);
            cv.setTelephoneCandidat(telephone);
            cv.setExperiencesDetectees(experiences);
            cv.setFormationDetectee(formation);
            cv.setSource(source);
            cv.setUploadedBy(uploader);
            cv.setCommentaireInitial(commentaire);

            if (demandeId != null) {
                DemandeRecrutement demande = demandeRepository.findById(demandeId).orElseThrow();
                cv.setDemande(demande);
            }

            cvRepository.save(cv);

            // Le CV est directement assigné à une demande : il entre automatiquement
            // dans le pipeline par défaut (étape RH).
            if (demandeId != null) {
                candidateApplicationService.getOrCreateApplication(cv.getId(), demandeId);
            }

            return ResponseEntity.ok(toResponse(cv, doublon));

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors du traitement du fichier : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vivier")
    public ResponseEntity<List<CvUploadResponse>> getVivierGeneral() {
        List<CvUploadResponse> cvs = cvRepository.findByDemandeIsNull().stream()
                .map(cv -> toResponse(cv, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(cvs);
    }

    @GetMapping("/demande/{demandeId}")
    public ResponseEntity<List<CvUploadResponse>> getByDemande(@PathVariable Long demandeId) {
        List<CvUploadResponse> cvs = cvRepository.findByDemandeId(demandeId).stream()
                .map(cv -> toResponse(cv, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(cvs);
    }

    @PatchMapping("/{id}/rattacher/{demandeId}")
    public ResponseEntity<CvUploadResponse> rattacherADemande(@PathVariable Long id, @PathVariable Long demandeId) {
        Cv cv = cvRepository.findById(id).orElseThrow();
        DemandeRecrutement demande = demandeRepository.findById(demandeId).orElseThrow();
        cv.setDemande(demande);
        cvRepository.save(cv);

        // Assignation du CV à une demande = entrée automatique dans le pipeline par défaut (étape RH).
        candidateApplicationService.getOrCreateApplication(id, demandeId);

        return ResponseEntity.ok(toResponse(cv, false));
    }

    @PatchMapping("/{id}/ajouter-vivier")
    public ResponseEntity<CvUploadResponse> ajouterAuVivier(@PathVariable Long id) {
        Cv cv = cvRepository.findById(id).orElseThrow();
        cv.setDemande(null);
        cvRepository.save(cv);
        return ResponseEntity.ok(toResponse(cv, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Réservé à l'admin — vérification de rôle à ajouter via @PreAuthorize si besoin
        cvRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CvUploadResponse toResponse(Cv cv, boolean doublon) {
        return new CvUploadResponse(
                cv.getId(),
                cv.getNomFichierOriginal(),
                cv.getNomCandidat(),
                cv.getEmailCandidat(),
                cv.getTelephoneCandidat(),
                cv.getCompetencesDetectees(),
                cv.getExperiencesDetectees(),
                cv.getFormationDetectee(),
                cv.getSource(),
                cv.getDemande() != null ? cv.getDemande().getId() : null,
                cv.getDemande() != null ? cv.getDemande().getTitre() : null,
                cv.getDateUpload(),
                doublon,
                cv.getTexteExtrait()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CvUploadResponse> getById(@PathVariable Long id) {
        return cvRepository.findById(id)
                .map(cv -> ResponseEntity.ok(toResponse(cv, false)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable Long id) throws java.io.IOException {
        Cv cv = cvRepository.findById(id).orElseThrow();

        java.nio.file.Path path = java.nio.file.Paths.get(cv.getFichierPath());
        org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = cv.getNomFichierOriginal().toLowerCase().endsWith(".pdf")
                ? "application/pdf"
                : "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + cv.getNomFichierOriginal() + "\"")
                .body(resource);
    }
}