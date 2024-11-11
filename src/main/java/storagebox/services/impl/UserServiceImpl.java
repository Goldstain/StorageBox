package storagebox.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import storagebox.entities.users.UserEntity;
import storagebox.repositories.UserRepository;
import storagebox.services.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public void saveUser(UserEntity userEntity) {
        userEntity.setPassword(encoder().encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }
}
