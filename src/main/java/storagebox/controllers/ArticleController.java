package storagebox.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
import storagebox.services.impl.S3Service;

import java.io.IOException;
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
    private final S3Service s3Service;


    @Autowired
    public ArticleController(
            ArticleService articleService, CategoryService categoryService
            , ExchangeRateService exchangeRateService, S3Service s3Service) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.exchangeRateService = exchangeRateService;
        this.s3Service = s3Service;
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
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile multipartFile,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "new-article";
        }

        String imageURL = null;
        if (!multipartFile.isEmpty()) {
            imageURL = validFileAndGetImageURL(multipartFile, model);
            if (imageURL == null) return "new-article";
        }
        if (imageURL != null) {
            article.setUrl(imageURL);
        }

        try {
            Category category = categoryService.findById(article.getCategory().getId());
            article.setCategory(category);
        } catch (CategoryNotFoundException e) {
            bindingResult.rejectValue("category", "error.category", e.getMessage());
            s3Service.deleteFromS3(imageURL);
            return "new-article";
        }

        articleService.save(article);
        return "redirect:/articles";
    }

    private String validFileAndGetImageURL(MultipartFile multipartFile, Model model) {
        String imageURL = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String contentType = multipartFile.getContentType();
            List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/gif");
            if (!allowedTypes.contains(contentType)) {
                model.addAttribute("error", "Непідтримуваний тип файлу. Дозволені лише JPEG, PNG і GIF");
                return null;
            } else if (multipartFile.getSize() > 20_971_000L) {
                model.addAttribute("error", "Файл має бути не більше 20 MB");
                return null;
            }

            try {
                imageURL = s3Service.uploadToS3(multipartFile, contentType);
                return imageURL;
            } catch (AmazonServiceException e) {
                model.addAttribute("error", "AWS Service Exception");
                e.printStackTrace();
            } catch (SdkClientException e) {
                model.addAttribute("error", "SDK Client Exception");
                e.printStackTrace();
            } catch (IOException e) {
                model.addAttribute("error", "Error Uploading file");
                e.printStackTrace();
            }
        }
        return "";
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
    public String editArticle(@PathVariable int id,
                              @Valid @ModelAttribute("article") Article article,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile multipartFile,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "edit-article";
        }

        String imageURL = validFileAndGetImageURL(multipartFile, model);

        if (imageURL == null) {
            model.addAttribute("article", article);
            return "edit-article";
        } else if (!imageURL.isEmpty()) {
            article.setUrl(imageURL);
        }

        String urlFromDB = "";
        try {
            Category category = categoryService.findById(article.getCategory().getId());
            article.setCategory(category);
            urlFromDB = articleService.findById(article.getId()).getUrl();
            articleService.update(id, article);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            article.setUrl(urlFromDB);
            model.addAttribute("article", article);
            model.addAttribute("errorCategory", "Категорію не знайдено");
            s3Service.deleteFromS3(imageURL);
            return "edit-article";
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            article.setUrl(urlFromDB);
            model.addAttribute("article", article);
            model.addAttribute("errorArticle", "Товар не знайдено");
            s3Service.deleteFromS3(imageURL);
            return "edit-article";
        } catch (WrongValueException e) {
            e.printStackTrace();
            article.setUrl(urlFromDB);
            model.addAttribute("article", article);
            model.addAttribute("errorValue", "Значення не може бути більше за куплене");
            s3Service.deleteFromS3(imageURL);
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


    @DeleteMapping("/{id}/delete-photo")
    public String deletePhoto(@PathVariable int id
            , RedirectAttributes redirectAttributes) {

        try {
            Article article = articleService.findById(id);

            String photoUrl = article.getUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                s3Service.deleteFromS3(photoUrl);
            }

            article.setUrl("");

            articleService.save(article);
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message", "Фото видалено успішно!");
        return "redirect:/articles/" + id;
    }

}

