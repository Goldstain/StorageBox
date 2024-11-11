package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import storagebox.entities.users.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByEmail(String email);
}
