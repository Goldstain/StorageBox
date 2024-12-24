package storagebox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storagebox.dto.UserDTO;
import storagebox.entities.security.User;
import storagebox.repositories.security.UserRepository;
import storagebox.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user))
                .sorted((u1,u2)-> u1.getId().compareTo(u2.getId()))
                .toList();
    }

    @Override
    public UserDTO findById(long id) {
        return new UserDTO(userRepository.findById(id).orElse(new User()));
    }

    @Override
    public void update(long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(new User());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles());
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
