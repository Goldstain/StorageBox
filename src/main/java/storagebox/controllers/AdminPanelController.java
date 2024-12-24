package storagebox.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import storagebox.dto.UserDTO;
import storagebox.entities.security.Role;
import storagebox.entities.security.User;
import storagebox.services.AdminPanelService;
import storagebox.services.UserService;
import storagebox.services.impl.models.ArticlesGroupingByName;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@SessionAttributes("loggedUser")
public class AdminPanelController {

    private final AdminPanelService adminPanelService;
    private final UserService userService;

    @Autowired
    public AdminPanelController(AdminPanelService adminPanelService, UserService userService) {
        this.adminPanelService = adminPanelService;
        this.userService = userService;
    }


    @ModelAttribute("loggedUser")
    public UserDTO getLoggedUser(Principal principal) {
        if (principal != null) {
            User user = (User) ((Authentication) principal).getPrincipal();
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            return userDTO;
        }
        return null;
    }


    @GetMapping("/statistics")
    public String getStatistics(
            @RequestParam(defaultValue = "profit") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection,
            Model model) {
        List<ArticlesGroupingByName> articles = adminPanelService.findAllArticles(sortField, sortDirection);
        ArticlesGroupingByName total = adminPanelService.getTotal();

        model.addAttribute("articles", articles);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("total", total);
        return "statistics";
    }


    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("usersDTO", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        model.addAttribute("userDTO", userService.findById(id));
        model.addAttribute("roles", Role.values());
        return "edit-user";
    }


    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable Long id
            , @Valid @ModelAttribute("userDTO") UserDTO userDTO
            , BindingResult bindingResult
            , @RequestParam Set<String> roles
            , Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", Role.values());
            return "edit-user";
        }

        userDTO.setRoles(roles);
        userService.update(id, userDTO);
        return "redirect:/admin/users";
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

}
