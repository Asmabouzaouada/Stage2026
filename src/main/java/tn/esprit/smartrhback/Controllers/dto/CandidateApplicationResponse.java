package tn.esprit.smartrhback.Controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.smartrhback.entities.Decision;
import tn.esprit.smartrhback.entities.PipelineStage;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CandidateApplicationResponse {
    private Long id;
    private Long cvId;
    private String nomCandidat;
    private String emailCandidat;
    private Long demandeId;
    private String demandeTitre;
    private Double matchingScore;
    private PipelineStage stage;
    private Decision decision;
    private String rhComment;
    private String technicalComment;
    private String managerialComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}