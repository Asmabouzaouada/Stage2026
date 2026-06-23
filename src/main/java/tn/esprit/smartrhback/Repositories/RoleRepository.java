package tn.esprit.smartrhback.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.smartrhback.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}