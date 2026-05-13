package es.educastur.todo_app.controllers;

import es.educastur.todo_app.DTOs.DashboardResponse;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final TaskService taskService;

    @Operation(summary = "Ver estadísticas del dashboard", description = "Devuelve contadores de tareas completadas, pendientes, atrasadas, etc.")
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getDashboardStats(user));
    }
}