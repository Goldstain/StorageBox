package storagebox.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import storagebox.entities.security.User;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @Size(min=3,max = 30, message = "Довжина ім'я має бути від 3 до 30 символів")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Має складатись тільки з літер (a-z, A-Z, а-я, А-Я)")
    private String firstName;

    @Size(min=3,max = 30, message = "Довжина прізвища має бути від 3 до 30 символів")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Має складатись тільки з літер (a-z, A-Z, а-я, А-Я)")
    private String lastName;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    private Set<String> roles = new HashSet<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
