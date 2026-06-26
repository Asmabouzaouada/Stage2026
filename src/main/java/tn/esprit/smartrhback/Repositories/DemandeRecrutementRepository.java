package tn.esprit.smartrhback.Repositories;

import tn.esprit.smartrhback.entities.DemandeRecrutement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeRecrutementRepository extends JpaRepository<DemandeRecrutement, Long> {
    List<DemandeRecrutement> findByCreateurId(Long createurId);
    List<DemandeRecrutement> findByDemandeurId(Long demandeurId);
}