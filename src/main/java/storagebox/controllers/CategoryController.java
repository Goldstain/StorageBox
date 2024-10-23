package storagebox.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import storagebox.entities.Category;
import storagebox.exceptions.CategoryAlreadyExistsException;
import storagebox.services.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute(name = "categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/new-category")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "new-category";
    }

    @PostMapping
    public String saveCategory(@Valid @ModelAttribute Category category
            , BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", category);
            return "new-category";
        }
        try {
            categoryService.saveCategory(category);
        } catch (CategoryAlreadyExistsException e) {
            e.printStackTrace();
            return "redirect:/categories/new-category";
        }
        return "redirect:/articles";
    }

}
