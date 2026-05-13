package es.educastur.todo_app.controllers;

import es.educastur.todo_app.DTOs.CreateUserRequest;
import es.educastur.todo_app.DTOs.UserResponse;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Registro de usuario", description = "Endpoint público para crear cuenta")
    @PostMapping("/auth/register") 
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest request) {
        User saved = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(saved));
    }

    @Operation(summary = "Ver mi perfil")
    @GetMapping("/user/profile") 
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @Operation(summary = "Actualizar mi perfil")
    @PutMapping("/user/profile") 
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal User user,
            @RequestBody CreateUserRequest request) {
        User updated = userService.updateUser(user, request);
        return ResponseEntity.ok(UserResponse.of(updated));
    }
}