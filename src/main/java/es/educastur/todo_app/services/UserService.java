package es.educastur.todo_app.services;

import es.educastur.todo_app.DTOs.CreateUserRequest;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.models.UserRole;
import es.educastur.todo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public User registerUser(CreateUserRequest request) {

        return userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .password(encoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .fullname(request.getFullname())
                        .role(UserRole.USER)
                        .build()
        );

    }

    public User changeRole(User user, UserRole userRole) {
        user.setRole(userRole);
        return userRepository.save(user);
    }

    public User changeRole(Long userId, UserRole userRole) {
        return userRepository.findById(userId)
                .map(u -> {
                    u.setRole(userRole);
                    return userRepository.save(u);
                }).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("username"));
    }

    public User updateUser(User user, CreateUserRequest request) {
        user.setBio(request.getBio());
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }
        
        return userRepository.save(user);
    }

    // Buscar usuario por ID lanzando excepción si no existe (crea la UserNotFoundException si no la tienes)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); 
    }

    // Actualizar usuario por ID (El método que tenías recibía el objeto entero, este recibe el ID)
    public User updateUserById(Long id, CreateUserRequest request) {
        User user = findById(id);
        user.setBio(request.getBio());
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        return userRepository.save(user);
    }

    // Borrar usuario
    public void deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

}

