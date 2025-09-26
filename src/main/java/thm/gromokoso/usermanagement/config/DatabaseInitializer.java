package thm.gromokoso.usermanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import thm.gromokoso.usermanagement.entity.ESystemRole;
import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.repository.UserRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final String SYSADMIN = "SYSADMIN";

    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Check if Admin user is already in Database
        if (!userRepository.existsById(SYSADMIN)) {
            User sysAdmin = new User();
            sysAdmin.setUserName(SYSADMIN);
            sysAdmin.setFirstName("");
            sysAdmin.setLastName("");
            sysAdmin.setEmail("");
            sysAdmin.setSystemRole(ESystemRole.ADMIN);

            userRepository.save(sysAdmin);
            System.out.println("SysAdmin user created.");
        } else {
            System.out.println("SysAdmin user already exists, skipping initialization.");
        }
    }
}
