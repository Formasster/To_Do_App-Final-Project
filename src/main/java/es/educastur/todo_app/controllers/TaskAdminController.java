package es.educastur.todo_app.controllers;

import es.educastur.todo_app.DTOs.EditTaskRequest;
import es.educastur.todo_app.models.Task;
import es.educastur.todo_app.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/tasks")
@PreAuthorize("hasRole('ADMIN')")
public class TaskAdminController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> editTask(
            @PathVariable Long id,
            @RequestBody EditTaskRequest req) {
        req.setId(id);
        return ResponseEntity.ok(taskService.editTaskAdmin(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteByIdAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.findAllByUserId(userId));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleCompleteAdmin(id));
    }
}