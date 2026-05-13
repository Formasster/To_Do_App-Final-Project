package es.educastur.todo_app.shared.init;

import es.educastur.todo_app.DTOs.CreateTaskRequest;
import es.educastur.todo_app.DTOs.CreateUserRequest;
import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.models.UserRole;
import es.educastur.todo_app.repositories.CategoryRepository;
import es.educastur.todo_app.repositories.UserRepository;
import es.educastur.todo_app.services.TaskService;
import es.educastur.todo_app.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final CategoryRepository categoryRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final UserRepository userRepository; 

    @PostConstruct
    public void init() {
        insertCategories();
        List<User> users = insertUsers();
        
        // Solo insertamos tareas si hemos recuperado/creado al menos un usuario
        if (!users.isEmpty()) {
            insertTasks(users.get(0));
        }
    }

    private List<User> insertUsers() {
        List<User> result = new ArrayList<>();

        // --- REGISTRO DE USUARIO NORMAL ---
        Optional<User> existingUser = userRepository.findByUsername("user");
        if (existingUser.isEmpty()) {
            CreateUserRequest req = CreateUserRequest.builder()
                    .username("user")
                    .email("user@user.com")
                    .password("1234")
                    .verifyPassword("1234")
                    .fullname("The user")
                    .build();
            User newUser = userService.registerUser(req);
            result.add(newUser);
        } else {
            result.add(existingUser.get());
        }

        // --- REGISTRO DE ADMINISTRADOR ---
        Optional<User> existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin.isEmpty()) {
            CreateUserRequest req2 = CreateUserRequest.builder()
                    .username("admin")
                    .email("admin@openwebinars.net")
                    .password("1234")
                    .verifyPassword("1234")
                    .fullname("Administrador")
                    .build();
            User admin = userService.registerUser(req2);
            userService.changeRole(admin, UserRole.ADMIN);
        }

        return result;
    }

    private void insertCategories() {
        // Comprobamos si la categoría Main ya existe (por ID o por nombre)
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder().title("Main").build());
        }
    }

    private void insertTasks(User author) {
        try {
            taskService.findAllByUser(author);
        } catch (Exception e) {
            // Si salta la excepción (EmptyTaskList), es que no hay tareas, así que las crea
            CreateTaskRequest req1 = CreateTaskRequest.builder()
                    .title("First task!")
                    .description("Lorem ipsum dolor sit amet")
                    .tags("tag1,tag2,tag3")
                    .build();

            taskService.createTask(req1, author);

            CreateTaskRequest req2 = CreateTaskRequest.builder()
                    .title("Second task!")
                    .description("Lorem ipsum dolor sit amet")
                    .tags("tag1,tag2,tag4")
                    .build();

            taskService.createTask(req2, author);
        }
    }
}
