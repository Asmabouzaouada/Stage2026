package tn.esprit.smartrhback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demandes_recrutement")
public class DemandeRecrutement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    private List<String> competencesRequises;

    @ElementCollection
    private List<String> competencesSouhaitees;

    @Enumerated(EnumType.STRING)
    private NiveauExperience niveauExperience;

    private LocalDate dateBesoin;

    private boolean urgence = false;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.OUVERTE;

    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    private User demandeur;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private User createur;

    private LocalDate dateCreation = LocalDate.now();
}