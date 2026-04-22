package es.educastur.todo_app.user.service;

import es.educastur.todo_app.user.dto.CreateUserRequest;
import es.educastur.todo_app.user.model.User;
import es.educastur.todo_app.user.model.UserRepository;
import es.educastur.todo_app.user.model.UserRole;
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

}

