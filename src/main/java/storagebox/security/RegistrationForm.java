package storagebox.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import storagebox.entities.security.Role;
import storagebox.entities.security.User;

import java.util.Set;

@NoArgsConstructor
@Data
public class RegistrationForm {

    @Size(min = 3, max = 30, message = "The name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Name must contain only letters (a-z, A-Z, а-я, А-Я)")
    private String firstName;

    @Size(min = 3, max = 30, message = "The surname must be between 3 and 30 characters long")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Name must contain only letters (a-z, A-Z, а-я, А-Я)")
    private String lastName;

    @Email(message = "This is not an email format")
    private String email;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        Set<String> roles = Set.of(Role.ROLE_MANAGER.toString());
        return new User(
                firstName, lastName, email, passwordEncoder.encode(password)
                , roles);
    }

}
