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
@Table(name = "cvs")
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fichierPath;
    private String nomFichierOriginal;

    @Column(length = 10000)
    private String texteExtrait;

    private String nomCandidat;
    private String emailCandidat;
    private String telephoneCandidat;

    @ElementCollection
    private List<String> competencesDetectees;

    @Column(length = 3000)
    private String experiencesDetectees;

    @Column(length = 1000)
    private String formationDetectee;

    @Enumerated(EnumType.STRING)
    private SourceCondidat source;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private DemandeRecrutement demande; // nullable : null = vivier général

    @ManyToOne
    @JoinColumn(name = "uploaded_by_id")
    private User uploadedBy;

    @Column(length = 1000)
    private String commentaireInitial;

    private LocalDate dateUpload = LocalDate.now();
}