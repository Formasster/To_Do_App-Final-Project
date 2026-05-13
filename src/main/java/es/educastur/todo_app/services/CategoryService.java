package es.educastur.todo_app.services;

import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskService taskService;

    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("title").ascending());
    }

    public void deleteById(Long id) {
        if (id != 1L) {
            Category oldCategory = categoryRepository.getReferenceById(id);
            Category mainCategory = categoryRepository.getReferenceById(1L);
            taskService.updateCategory(oldCategory, mainCategory);
            categoryRepository.deleteById(id);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

}
