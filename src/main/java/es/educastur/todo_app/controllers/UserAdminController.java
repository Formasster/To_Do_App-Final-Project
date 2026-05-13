package es.educastur.todo_app.controllers;

import es.educastur.todo_app.DTOs.CreateUserRequest;
import es.educastur.todo_app.DTOs.UserResponse;
import es.educastur.todo_app.models.UserRole;
import es.educastur.todo_app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequiredArgsConstructor
@RequestMapping("/admin/users") 
@PreAuthorize("hasRole('ADMIN')") 
public class UserAdminController {

    private final UserService userService;

    // REQUISITO: GET /admin/users
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserResponse> users = userService.findAll()
                .stream()
                .map(UserResponse::of)
                .toList();
        return ResponseEntity.ok(users);
    }

    // REQUISITO: GET /admin/users/{id}
    @Operation(summary = "Ver un usuario concreto")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.of(userService.findById(id)));
    }

    // REQUISITO: POST /admin/users/{id}/promote
    @Operation(summary = "Promover usuario a GESTOR")
    @PostMapping("/{id}/promote")
    public ResponseEntity<UserResponse> promoteToGestor(@PathVariable Long id) {
        // Suponiendo que el rol intermedio en tu enum se llama GESTOR (o modifícalo a MANAGER si lo llamaste así)
        return ResponseEntity.ok(UserResponse.of(userService.changeRole(id, UserRole.GESTOR))); 
    }

    // REQUISITO: POST /admin/users/{id}/demote
    @Operation(summary = "Degradar GESTOR a usuario")
    @PostMapping("/{id}/demote")
    public ResponseEntity<UserResponse> demoteToUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.of(userService.changeRole(id, UserRole.USER)));
    }

    // REQUISITO: PUT /admin/users/{id}
    @Operation(summary = "Editar un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> editUser(
            @PathVariable Long id, 
            @RequestBody CreateUserRequest request) { 
        return ResponseEntity.ok(UserResponse.of(userService.updateUserById(id, request)));
    }

    // REQUISITO: DELETE /admin/users/{id}
    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}