package es.educastur.todo_app.controllers;

import es.educastur.todo_app.DTOs.CreateTaskRequest;
import es.educastur.todo_app.DTOs.EditTaskRequest;
import es.educastur.todo_app.models.Task;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task") 
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Listar todas las tareas", description = "Devuelve todas las tareas del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<Task>> getMyTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findAllByUser(user));
    }

    @Operation(summary = "Ver una tarea concreta", description = "Devuelve los detalles de una tarea si pertenece al usuario")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findByIdAndUser(id, user));
    }

    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea asociada automáticamente al usuario autenticado")
    @PostMapping
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody CreateTaskRequest req,
            @AuthenticationPrincipal User author) {
        Task created = taskService.createTask(req, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Editar tarea", description = "Edita una tarea existente (solo si es del usuario autenticado)")
    @PutMapping("/{id}")
    public ResponseEntity<Task> editTask(
            @PathVariable Long id,
            @Valid @RequestBody EditTaskRequest req,
            @AuthenticationPrincipal User user) {
        req.setId(id);
        return ResponseEntity.ok(taskService.editTask(req, user));
    }

    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea (solo si es del usuario autenticado)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        taskService.deleteById(id, user);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // ENDPOINTS DE BÚSQUEDA 
    // =========================================================================

    @GetMapping(value = "/search", params = "title")
    public ResponseEntity<List<Task>> searchByTitle(
            @RequestParam String title,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findByTitleAndUser(title, user));
    }

    @GetMapping(value = "/search", params = "completed")
    public ResponseEntity<List<Task>> searchByCompleted(
            @RequestParam boolean completed,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findByCompletedAndUser(completed, user));
    }

    @GetMapping(value = "/search", params = "category")
    public ResponseEntity<List<Task>> searchByCategory(
            @RequestParam String category,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findByCategoryNameAndUser(category, user));
    }

    @GetMapping(value = "/search", params = "priority")
    public ResponseEntity<List<Task>> searchByPriority(
            @RequestParam String priority,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findByPriorityAndUser(priority, user));
    }

    // =========================================================================
    // ENDPOINTS PARA LA GESTIÓN DE TAGS
    // =========================================================================

    // REQUISITO: POST /task/{id}/tags
    @PostMapping("/{id}/tags")
    public ResponseEntity<Task> addTagToTask(
            @PathVariable Long id,
            @RequestParam Long tagId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.addTagToTask(id, tagId, user));
    }

    // REQUISITO: DELETE /task/{id}/tags/{tagId}
    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Task> removeTagFromTask(
            @PathVariable("id") Long taskId,
            @PathVariable Long tagId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.removeTagFromTask(taskId, tagId, user));
    }

    // REQUISITO: GET /task/by-tag?tag=...
    @GetMapping(value = "/by-tag", params = "tag")
    public ResponseEntity<List<Task>> getTasksByTag(
            @RequestParam("tag") Long tagId, 
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.findTasksByTagAndUser(tagId, user));
    }

    // =========================================================================
    // ENDPOINTS EXTRAS 
    // =========================================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.toggleComplete(id, user));
    }

    @PatchMapping("/{id}/star")
    public ResponseEntity<Task> toggleStar(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.toggleStar(id, user));
    }
}