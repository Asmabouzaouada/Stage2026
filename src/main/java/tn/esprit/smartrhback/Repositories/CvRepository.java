package tn.esprit.smartrhback.Repositories;

import tn.esprit.smartrhback.entities.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CvRepository extends JpaRepository<Cv, Long> {
    List<Cv> findByDemandeIsNull(); // vivier général
    List<Cv> findByDemandeId(Long demandeId);
    Optional<Cv> findByEmailCandidat(String email);
}