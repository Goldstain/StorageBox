package storagebox.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import storagebox.entities.security.Role;
import storagebox.entities.security.User;
import storagebox.repositories.security.UserRepository;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(
                    "admin",
                    "admin",
                    adminLogin,
                    passwordEncoder.encode(adminPassword),
                    Set.of(Role.ROLE_ADMIN.toString())
            );
            userRepository.save(admin);
            System.out.println("Admin created with login: " + adminLogin
                    + " and password: " + adminPassword);
        }
    }
}
