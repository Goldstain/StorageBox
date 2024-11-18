package storagebox.controllers.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import storagebox.entities.security.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false)
                        String error, Model model) {
        model.addAttribute("user", new User());

        if (error != null) {
            model.addAttribute(
                    "error", "Invalid username or password");
        }
        return "login";
    }

}
