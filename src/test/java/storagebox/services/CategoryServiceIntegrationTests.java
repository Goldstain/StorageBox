package storagebox.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import storagebox.entities.Category;
import storagebox.exceptions.CategoryAlreadyExistsException;
import storagebox.exceptions.CategoryNotFoundException;
import storagebox.repositories.CategoryRepository;

import java.util.List;

@SpringBootTest
@Transactional
public class CategoryServiceIntegrationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldSaveAndFindCategory() throws CategoryAlreadyExistsException {

        List<Category> categories = categoryService.findAll();

        Category testCategory = new Category("SpringBootTestCategory");

        categoryService.saveCategory(testCategory);

        List<Category> updatedCategories = categoryService.findAll();
        assertEquals(categories.size() + 1, updatedCategories.size());

        Category lastCategory = updatedCategories.get(updatedCategories.size() - 1);
        assertEquals(testCategory.getName(), lastCategory.getName());

    }

    @Test
    public void shouldThrowsExceptionWhenCategoryAlreadyExists() throws CategoryAlreadyExistsException {

        List<Category> categories = categoryService.findAll();
        Category testCategory;

        if (categories.size() > 0) {
            testCategory = categories.get(0);
        } else {
            testCategory = new Category("SpringBootTestCategory");
            categoryService.saveCategory(testCategory);
        }

        assertThrows(CategoryAlreadyExistsException.class
                , () -> categoryService.saveCategory(testCategory));
    }

    @Test
    public void shouldThrowsExceptionWhenCategoryNotFound() throws CategoryNotFoundException {
        int listSize = categoryRepository.findAll().size();

        assertThrows(CategoryNotFoundException.class
                , () -> categoryService.findById(listSize + 1));
    }
}
