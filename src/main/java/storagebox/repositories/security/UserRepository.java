package storagebox.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import storagebox.entities.security.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);
}
