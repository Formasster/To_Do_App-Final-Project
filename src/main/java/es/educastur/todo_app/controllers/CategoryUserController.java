package es.educastur.todo_app.controllers;

import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories") 
public class CategoryUserController {

    private final CategoryService categoryService;

    @Operation(summary = "Listar todas las categorías", description = "Visible para cualquier usuario autenticado")
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Ver detalle de una categoría")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }
}