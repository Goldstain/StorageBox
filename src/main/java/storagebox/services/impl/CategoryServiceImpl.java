package storagebox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storagebox.entities.Category;
import storagebox.exceptions.CategoryAlreadyExistsException;
import storagebox.exceptions.CategoryNotFoundException;
import storagebox.repositories.CategoryRepository;
import storagebox.services.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(int id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(
                "Категорію з id " + id + " не знайдено"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void saveCategory(Category category) throws CategoryAlreadyExistsException {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException("Ця категорія вже існує");
        }
        categoryRepository.save(category);
    }
}
