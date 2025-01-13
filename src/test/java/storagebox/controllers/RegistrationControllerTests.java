package storagebox.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import storagebox.controllers.security.RegistrationController;
import storagebox.entities.security.User;
import storagebox.security.RegistrationForm;
import storagebox.services.UserService;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    public void shouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("registrationForm"))
                .andExpect(model().attribute("registrationForm", instanceOf(RegistrationForm.class)));
    }


    @Test
    public void shouldRedirectToLoginView() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "doe@gmail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).save(any(User.class));

    }

    @Test
    public void shouldRedirectToRegistrationViewWhenFieldsIsIncorrect() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "doe")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors(
                        "registrationForm","firstName", "lastName", "email", "password"));
    }
}
