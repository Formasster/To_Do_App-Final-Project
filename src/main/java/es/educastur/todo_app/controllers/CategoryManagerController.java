package es.educastur.todo_app.controllers;

import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/categories") // REQUISITO: Ruta exacta
@PreAuthorize("hasAnyRole('GESTOR', 'ADMIN')") // REQUISITO: GESTOR o ADMIN
public class CategoryManagerController {

    private final CategoryService categoryService;

    @Operation(summary = "Listar categorías (Gestor)")
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Crear categoría (Gestor)")
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @Operation(summary = "Editar categoría (Gestor)")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return ResponseEntity.ok(categoryService.save(category));
    }

    @Operation(summary = "Eliminar categoría (Gestor)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}