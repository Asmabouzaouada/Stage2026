package tn.esprit.smartrhback.services;



import tn.esprit.smartrhback.Controllers.dto.CandidateApplicationResponse;
import tn.esprit.smartrhback.entities.*;
import tn.esprit.smartrhback.Repositories.CandidateApplicationRepository;
import tn.esprit.smartrhback.Repositories.CvRepository;
import tn.esprit.smartrhback.Repositories.DemandeRecrutementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateApplicationService {

    @Autowired
    private CandidateApplicationRepository applicationRepository;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private DemandeRecrutementRepository demandeRepository;

    public CandidateApplicationResponse createApplication(Long cvId, Long demandeId) {
        Cv cv = cvRepository.findById(cvId).orElseThrow(() -> new RuntimeException("CV introuvable"));
        DemandeRecrutement demande = demandeRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande introuvable"));

        CandidateApplication app = new CandidateApplication();
        app.setCv(cv);
        app.setDemande(demande);
        app.setMatchingScore(0.0);
        app.setStage(PipelineStage.RH);
        app.setDecision(Decision.PENDING);
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        applicationRepository.save(app);
        return toResponse(app);
    }

    public List<CandidateApplicationResponse> getByDemande(Long demandeId) {
        return applicationRepository.findByDemandeId(demandeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<CandidateApplicationResponse> getByCv(Long cvId) {
        return applicationRepository.findByCvId(cvId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CandidateApplicationResponse updateDecision(Long id, Decision decision, String comment) {
        CandidateApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application introuvable"));

        app.setDecision(decision);
        app.setUpdatedAt(LocalDateTime.now());

        switch (app.getStage()) {
            case RH -> {
                app.setRhComment(comment);
                if (decision == Decision.GO) app.setStage(PipelineStage.TECHNICAL);
                else app.setStage(PipelineStage.VIVIER);
            }
            case TECHNICAL -> {
                app.setTechnicalComment(comment);
                if (decision == Decision.GO) app.setStage(PipelineStage.MANAGERIAL);
                else app.setStage(PipelineStage.VIVIER);
            }
            case MANAGERIAL -> {
                app.setManagerialComment(comment);
                if (decision == Decision.GO) app.setStage(PipelineStage.HIRED);
                else app.setStage(PipelineStage.VIVIER);
            }
            default -> {}
        }

        applicationRepository.save(app);
        return toResponse(app);
    }

    private CandidateApplicationResponse toResponse(CandidateApplication app) {
        return new CandidateApplicationResponse(
                app.getId(),
                app.getCv().getId(),
                app.getCv().getNomCandidat(),
                app.getCv().getEmailCandidat(),
                app.getDemande().getId(),
                app.getDemande().getTitre(),
                app.getMatchingScore(),
                app.getStage(),
                app.getDecision(),
                app.getRhComment(),
                app.getTechnicalComment(),
                app.getManagerialComment(),
                app.getCreatedAt(),
                app.getUpdatedAt()
        );
    }
}