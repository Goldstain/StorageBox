package storagebox.services;

import storagebox.entities.Category;
import storagebox.exceptions.CategoryAlreadyExistsException;
import storagebox.exceptions.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    public Category findById(int id) throws CategoryNotFoundException;

    public List<Category> findAll();

    public void saveCategory(Category category) throws CategoryAlreadyExistsException;
}
