package tn.esprit.smartrhback.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.smartrhback.entities.Role;
import tn.esprit.smartrhback.Repositories.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if(roleRepository.findByName("ADMIN").isEmpty()) {

            Role role = new Role();
            role.setName("ADMIN");

            roleRepository.save(role);
        }

        if(roleRepository.findByName("HEAD_HUNTER").isEmpty()) {

            Role role = new Role();
            role.setName("HEAD_HUNTER");

            roleRepository.save(role);
        }

        if(roleRepository.findByName("TEAM_MANAGER").isEmpty()) {

            Role role = new Role();
            role.setName("TEAM_MANAGER");

            roleRepository.save(role);
        }
    }
}
