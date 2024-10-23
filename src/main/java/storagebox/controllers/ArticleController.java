package storagebox.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import storagebox.entities.Article;
import storagebox.entities.Category;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.CategoryNotFoundException;
import storagebox.services.ArticleService;
import storagebox.services.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;

    @Autowired
    public ArticleController(
            ArticleService articleService, CategoryService categoryService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }


    @ModelAttribute(name = "categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping
    public String getArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
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
    public String editArticle(@PathVariable int id, @Valid @ModelAttribute("article") Article article
            , BindingResult bindingResult, Model model) {
        Article articleFromDB;
        try {
            articleFromDB = articleService.findById(id);
            article.setCategory(articleFromDB.getCategory());
            article.setCreatedDate(articleFromDB.getCreatedDate());
            article.setProfit(article.getSellingPrize() - article.getSpentMoney()
                    - article.getPurchase() * article.getSoldQuantity());
            article.setRemainder(article.getQuantity() - article.getSoldQuantity());

        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            return "redirect:/articles";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "edit-article";
        }

        try {
            articleService.update(id, article);
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            return "redirect:/articles";
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
