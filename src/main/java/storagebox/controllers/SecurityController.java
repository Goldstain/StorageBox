package storagebox.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import storagebox.entities.users.UserEntity;
import storagebox.services.UserService;

@Controller
@AllArgsConstructor
public class SecurityController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userEntity") @Valid
                               UserEntity userEntity, BindingResult bindingResult) {
        userService.saveUser(userEntity);
        return "redirect:/login";
    }
}
