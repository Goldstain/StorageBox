package storagebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storagebox.entities.Category;
import storagebox.exceptions.CategoryAlreadyExistsException;
import storagebox.exceptions.CategoryNotFoundException;
import storagebox.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
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

