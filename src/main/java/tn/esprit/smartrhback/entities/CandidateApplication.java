package tn.esprit.smartrhback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "candidate_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private DemandeRecrutement demande;

    private Double matchingScore;

    @Enumerated(EnumType.STRING)
    private PipelineStage stage;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    private String rhComment;

    private String technicalComment;

    private String managerialComment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}