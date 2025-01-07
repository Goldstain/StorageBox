package storagebox.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import storagebox.dto.UserDTO;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.entities.Category;
import storagebox.entities.security.User;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.CategoryNotFoundException;
import storagebox.exceptions.WrongValueException;
import storagebox.services.ArticleService;
import storagebox.services.CategoryService;
import storagebox.services.ExchangeRateService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/articles")
@SessionAttributes("loggedUser")
public class ArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ArticleController(
            ArticleService articleService, CategoryService categoryService, ExchangeRateService exchangeRateService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.exchangeRateService = exchangeRateService;
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

    @ModelAttribute(name = "currencyData")
    public Map<String, Double> currencyData() {
        return exchangeRateService.getExchangeRateMap();
    }


    @ModelAttribute(name = "categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute(name = "statuses")
    public List<ArticleStatus> getStatuses() {
        return Arrays.asList(ArticleStatus.values());
    }

    @ModelAttribute(name = "searchName")
    public String getSearchName() {
        return new String("");
    }

    @GetMapping
    public String getArticlesINSTOCK(Model model) {
        model.addAttribute("articles", articleService.findAllByStatus(ArticleStatus.IN_STOCK));
        return "articles";
    }

    @GetMapping("/all-articles")
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles";
    }

    @GetMapping("/filter-by-status")
    public String filterByStatus(@RequestParam("status") ArticleStatus status, Model model) {
        if (status == null) {
            model.addAttribute("status", status);
            return "redirect:/articles";
        }
        List<Article> articles = articleService.findAllByStatus(status);

        model.addAttribute("articles", articles);
        model.addAttribute("statuses", ArticleStatus.values());
        model.addAttribute("selectedStatus", status);
        return "articles";
    }

    @GetMapping("/search-by-name")
    public String searchByName(@RequestParam("searchName") String searchName, Model model) {
        model.addAttribute("articles", articleService.findAllByName(searchName));
        return "articles";
    }


    @GetMapping("/new-article")
    public String newArticle(@ModelAttribute("article") Article article) {
        return "new-article";
    }


    @PostMapping
    public String saveArticle(@Valid @ModelAttribute("article") Article article,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "new-article";
        }
        try {
            Category category = categoryService.findById(article.getCategory().getId());
            article.setCategory(category);
        } catch (CategoryNotFoundException e) {
            bindingResult.rejectValue("category", "error.category", e.getMessage());
            return "new-article";
        }

        articleService.save(article);
        return "redirect:/articles";
    }

    @GetMapping("/{id}")
    public String getArticle(@PathVariable int id, Model model) {
        try {
            Article articleById = articleService.findById(id);
            model.addAttribute("article", articleById);
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            return "redirect:/articles";
        }
        return "edit-article";
    }


    @PutMapping("/{id}")
    public String editArticle(@PathVariable int id, @Valid @ModelAttribute("article") Article article,
                              BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors() && !
                bindingResult.getFieldError("category").getField().equals("category")) {
            model.addAttribute("article", article);
            return "edit-article";
        }
        try {
            Category category = categoryService.findById(article.getCategory().getId());
            article.setCategory(category);
            articleService.update(id, article);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("article", article);
            model.addAttribute("error-message", "Категорію не знайдено");
            return "edit-article";
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("article", article);
            model.addAttribute("error-message", "Товар не знайдено");
            return "edit-article";
        } catch (WrongValueException e) {
            e.printStackTrace();
            model.addAttribute("article", article);
            model.addAttribute("error-message", "Значення не може бути більше за куплене");
            return "edit-article";
        }

        return "redirect:/articles";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable int id) {
        try {
            articleService.delete(id);
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            return "redirect:/articles/edit-article";
        }
        return "redirect:/articles";
    }
}

