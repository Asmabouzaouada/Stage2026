package tn.esprit.smartrhback.Controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.smartrhback.entities.NiveauExperience;
import tn.esprit.smartrhback.entities.StatutDemande;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DemandeResponse {
    private Long id;
    private String titre;
    private String description;
    private List<String> competencesRequises;
    private List<String> competencesSouhaitees;
    private NiveauExperience niveauExperience;
    private LocalDate dateBesoin;
    private boolean urgence;
    private StatutDemande statut;
    private String demandeurNom;
    private String createurNom;
    private LocalDate dateCreation;
}