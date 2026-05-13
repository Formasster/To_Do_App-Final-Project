package es.educastur.todo_app.controllers;

import es.educastur.todo_app.models.Tag;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag") 
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Listar tags del usuario", description = "Devuelve todas las etiquetas")
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(tagService.findAllByUser(user));
    }

    @Operation(summary = "Crear un tag")
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tag));
    }

    @Operation(summary = "Editar un tag")
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.update(id, tag));
    }

    @Operation(summary = "Eliminar un tag")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}