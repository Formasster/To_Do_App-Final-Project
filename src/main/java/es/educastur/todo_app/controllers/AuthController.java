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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Registro de usuario", description = "Permite a nuevos usuarios crearse una cuenta (Público)")
    @PostMapping("/register") 
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(userService.registerUser(req)));
    }

    @Operation(summary = "Obtener usuario actual", description = "Devuelve los detalles del usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserResponse.of(user));
    }
}