package storagebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import storagebox.entities.Article;
import storagebox.entities.Category;
import storagebox.repositories.ArticleRepository;
import storagebox.services.ArticleService;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping
    public String getArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles";
    }

    @GetMapping("/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("categories", Category.values());
        return "newArticle";
    }

    @PostMapping
    public String saveArticle(@ModelAttribute("article") Article article) {
        articleService.save(article);
        return "redirect:/articles";
    }
}
