package es.educastur.todo_app.shared.init;

import es.educastur.todo_app.category.model.Category;
import es.educastur.todo_app.category.model.CategoryRepository;
import es.educastur.todo_app.task.dto.CreateTaskRequest;
import es.educastur.todo_app.task.service.TaskService;
import es.educastur.todo_app.user.dto.CreateUserRequest;
import es.educastur.todo_app.user.model.User;
import es.educastur.todo_app.user.model.UserRole;
import es.educastur.todo_app.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final CategoryRepository categoryRepository;
    private final TaskService taskService;
    private final UserService userService;

    @PostConstruct
    public void init() {
        insertCategories();
        List<User> users =insertUsers();
        insertTasks(users.get(0));
    }

    /*
        Solamente devuelve aquellos que son UserRole.USER
        para poder usarlos como autores de Task
     */
    private List<User> insertUsers() {

        List<User> result = new ArrayList<>();

        CreateUserRequest req = CreateUserRequest.builder()
                .username("user")
                .email("user@user.com")
                .password("1234")
                .verifyPassword("1234")
                .fullname("The user")
                .build();
        User user = userService.registerUser(req);
        result.add(user);

        CreateUserRequest req2 = CreateUserRequest.builder()
                .username("admin")
                .email("admin@openwebinars.net")
                .password("1234")
                .verifyPassword("1234")
                .fullname("Administrador")
                .build();
        User user2 = userService.registerUser(req2);

        userService.changeRole(user2, UserRole.ADMIN);

        return result;
    }

    private void insertCategories() {
        categoryRepository.save(Category.builder().title("Main").build());
    }

    private void insertTasks(User author) {

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
