package storagebox.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import storagebox.entities.security.User;
import storagebox.repositories.security.UserRepository;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isPresent()) return userOptional.get();
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/files/upload"))
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/articles/**", "/categories/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/", "/login", "/register").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images").permitAll()
                                .requestMatchers("/files/**").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/articles", true)
                                .permitAll())
                .build();
    }
}
