package tn.esprit.smartrhback.Controllers.dto;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.smartrhback.entities.NiveauExperience;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DemandeRequest {
    private String titre;
    private String description;
    private List<String> competencesRequises;
    private List<String> competencesSouhaitees;
    private NiveauExperience niveauExperience;
    private LocalDate dateBesoin;
    private boolean urgence;
    private Long demandeurId; // si null, le créateur devient automatiquement le demandeur
}