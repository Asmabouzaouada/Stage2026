package tn.esprit.smartrhback.Repositories;
import tn.esprit.smartrhback.entities.CandidateApplication;
import tn.esprit.smartrhback.entities.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, Long> {
    List<CandidateApplication> findByDemandeId(Long demandeId);
    List<CandidateApplication> findByStage(PipelineStage stage);
    List<CandidateApplication> findByCvId(Long cvId);
}