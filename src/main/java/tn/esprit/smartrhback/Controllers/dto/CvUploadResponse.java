package tn.esprit.smartrhback.Controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.smartrhback.entities.SourceCondidat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CvUploadResponse {
    private Long id;
    private String nomFichierOriginal;
    private String nomCandidat;
    private String emailCandidat;
    private String telephoneCandidat;
    private List<String> competencesDetectees;
    private String experiencesDetectees;
    private String formationDetectee;
    private SourceCondidat source;
    private Long demandeId;
    private String demandeTitre;
    private LocalDate dateUpload;
    private boolean doublonPotentiel;
}