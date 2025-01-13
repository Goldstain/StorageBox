package storagebox.services;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import storagebox.entities.security.Role;
import storagebox.entities.security.User;
import storagebox.repositories.security.UserRepository;
import storagebox.services.impl.UserServiceImpl;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return false when email does not exist")
    public void shouldReturnFalseWhenEmailDoesNotExist() {
        BDDMockito.given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        boolean result = userService.findByEmail(anyString());

        assertFalse(result);
        Mockito.verify(userRepository).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should return true when email exists")
    public void shouldReturnTrueWhenEmailExists() {
        User user = new User("John", "Doe", "doe@gmail.com"
                , "1111", Set.of(Role.ROLE_USER.name()));
        BDDMockito.given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        boolean result = userService.findByEmail(user.getEmail());

        assertTrue(result);
        Mockito.verify(userRepository).findByEmail(user.getEmail());
    }

}
