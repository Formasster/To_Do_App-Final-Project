package es.educastur.todo_app.task.service;

import es.educastur.todo_app.category.model.Category;
import es.educastur.todo_app.category.model.CategoryRepository;
import es.educastur.todo_app.tag.service.TagService;
import es.educastur.todo_app.task.dto.CreateTaskRequest;
import es.educastur.todo_app.task.dto.EditTaskRequest;
import es.educastur.todo_app.task.exception.EmptyTaskListException;
import es.educastur.todo_app.task.exception.TaskNotFoundException;
import es.educastur.todo_app.task.model.Task;
import es.educastur.todo_app.task.model.TaskRepository;
import es.educastur.todo_app.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;

        public List<Task> findAll() {

        List<Task> result = taskRepository.findAll();

        if (result.isEmpty())
            throw new EmptyTaskListException();

        return result;

    }

    private List<Task> findAll(User user) {

        List<Task> result = null;

        if (user != null)
            result = taskRepository.findByAuthor(user, Sort.by("createdAt").ascending());
        else
            result = taskRepository.findAll(Sort.by("createdAt").ascending());

        if (result.isEmpty())
            throw new EmptyTaskListException();

        return result;
    }

    public List<Task> findAllByUser(User user) {
        return findAll(user);
    }

    public List<Task> findAllAdmin() {
        return findAll(null);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    public Task createTask(CreateTaskRequest req, User author) {
        return createOrEditTask(req, author);
    }

    public Task editTask(EditTaskRequest req) {
        return createOrEditTask(req, null);
    }

    private Task createOrEditTask(CreateTaskRequest req, User author) {

        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .build();

        if (req.getCategoryId() == null || req.getCategoryId() == -1L)
            req.setCategoryId(1L);
        Category category = categoryRepository.getReferenceById(req.getCategoryId());
        if (category == null) // Categoría por defecto
            category = categoryRepository.getReferenceById(1L);

        task.setCategory(category);

        // Procesamos los tags que vienen en forma de tag1,tag2,tag3
        List<String> textTags = Arrays.stream(req.getTags().split(","))
                .map(String::trim)
                .toList();
        // Los añadimos a task
        task.getTags().addAll(tagService.saveOrGet(textTags));

        // Esto solamente se procesa si queremos editar un Task
        if (req instanceof EditTaskRequest editReq) {
            Task oldTask = findById(editReq.getId());
            task.setId(oldTask.getId());
            task.setCreatedAt(oldTask.getCreatedAt());
            task.setAuthor(oldTask.getAuthor());
            task.setCompleted(editReq.isCompleted());
        } else {
            task.setAuthor(author);
        }

        // Inserta o actualiza, según corresponda
        return taskRepository.save(task);

    }

    public Task toggleComplete(Long id) {
        Task task = findById(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> updateCategory(Category oldCategory, Category newCategory) {
        List<Task> tasks = taskRepository.findByCategory(oldCategory);
        tasks.forEach(t -> t.setCategory(newCategory));
        taskRepository.saveAll(tasks);
        return tasks;
    }

}
