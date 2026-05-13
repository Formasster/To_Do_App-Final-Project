package es.educastur.todo_app.services;

import es.educastur.todo_app.DTOs.CreateTaskRequest;
import es.educastur.todo_app.DTOs.DashboardResponse;
import es.educastur.todo_app.DTOs.EditTaskRequest;
import es.educastur.todo_app.exception.EmptyTaskListException;
import es.educastur.todo_app.exception.TaskNotFoundException;
import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.models.Priority;
import es.educastur.todo_app.models.Task;
import es.educastur.todo_app.models.TaskStatus;
import es.educastur.todo_app.models.User;
import es.educastur.todo_app.repositories.CategoryRepository;
import es.educastur.todo_app.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime; 
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;

    // =========================================================================
    // MÉTODO AUXILIAR DE SEGURIDAD (Regla de negocio centralizada)
    // =========================================================================
    private Task getTaskIfAuthorized(Long id, User user) {
        Task task = findById(id);
        if (!task.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a esta tarea");
        }
        return task;
    }

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

    // Nuevo método para el Controller que busca y valida la autoría
    public Task findByIdAndUser(Long id, User user) {
        return getTaskIfAuthorized(id, user);
    }

    public Task createTask(CreateTaskRequest req, User author) {
        return createOrEditTask(req, author);
    }

    public Task editTask(EditTaskRequest req, User user) {
        // Validamos que el usuario sea el dueño antes de editar
        getTaskIfAuthorized(req.getId(), user);
        return createOrEditTask(req, null);
    }

    private Task createOrEditTask(CreateTaskRequest req, User author) {
        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .deadline(req.getDeadline())
                .priority(req.getPriority() != null ? req.getPriority() : Priority.MEDIUM)
                .starred(req.isStarred())
                .notes(req.getNotes())
                .build();

        if (req.getCategoryId() == null || req.getCategoryId() == -1L)
            req.setCategoryId(1L);
        Category category = categoryRepository.getReferenceById(req.getCategoryId());
        if (category == null)
            category = categoryRepository.getReferenceById(1L);

        task.setCategory(category);

        List<String> textTags = Arrays.stream(req.getTags().split(","))
                .map(String::trim)
                .toList();
        task.getTags().addAll(tagService.saveOrGet(textTags));

        if (req instanceof EditTaskRequest editReq) {
            Task oldTask = findById(editReq.getId());
            task.setId(oldTask.getId());
            task.setCreatedAt(oldTask.getCreatedAt());
            task.setAuthor(oldTask.getAuthor());
            task.setCompleted(editReq.isCompleted());
            task.setStatus(editReq.getStatus() != null ? editReq.getStatus() : TaskStatus.PENDING);
        } else {
            task.setAuthor(author);
            task.setStatus(TaskStatus.PENDING);
        }

        return taskRepository.save(task);
    }

    public Task toggleComplete(Long id, User user) {
        Task task = getTaskIfAuthorized(id, user);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteById(Long id, User user) {
        Task task = getTaskIfAuthorized(id, user);
        taskRepository.delete(task); 
    }

    public List<Task> updateCategory(Category oldCategory, Category newCategory) {
        List<Task> tasks = taskRepository.findByCategory(oldCategory);
        tasks.forEach(t -> t.setCategory(newCategory));
        taskRepository.saveAll(tasks);
        return tasks;
    }

    public Task toggleStar(Long id, User user) {
        Task task = getTaskIfAuthorized(id, user);
        task.setStarred(!task.isStarred());
        return taskRepository.save(task);
    }

    public List<Task> findStarredByUser(User user) {
        List<Task> result = taskRepository.findByAuthorAndStarred(user, true, Sort.by("createdAt").ascending());
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    public List<Task> findByPriorityAndUser(String priority, User user) {
        Priority p = Priority.valueOf(priority.toUpperCase());
        List<Task> result = taskRepository.findByAuthorAndPriority(user, p, Sort.by("createdAt").ascending());
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    public List<Task> findByStatusAndUser(String status, User user) {
        TaskStatus s = TaskStatus.valueOf(status.toUpperCase());
        List<Task> result = taskRepository.findByAuthorAndStatus(user, s, Sort.by("createdAt").ascending());
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> result = taskRepository.findByAuthor_Id(userId, Sort.by("createdAt").ascending());
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    // =========================================================================
    // MÉTODOS PARA LA GESTIÓN DE ETIQUETAS
    // =========================================================================

    public Task addTagToTask(Long taskId, Long tagId, User user) {
        Task task = getTaskIfAuthorized(taskId, user);
        es.educastur.todo_app.models.Tag tag = tagService.findById(tagId);
        
        if(!task.getTags().contains(tag)) {
            task.getTags().add(tag);
            return taskRepository.save(task);
        }
        return task;
    }

    public Task removeTagFromTask(Long taskId, Long tagId, User user) {
        Task task = getTaskIfAuthorized(taskId, user);
        es.educastur.todo_app.models.Tag tag = tagService.findById(tagId);
        
        task.getTags().remove(tag);
        return taskRepository.save(task);
    }

    public List<Task> findTasksByTagAndUser(Long tagId, User user) {
        List<Task> result = taskRepository.findByAuthorAndTags_Id(user, tagId);
        
        if (result.isEmpty()) {
            throw new EmptyTaskListException();
        }
        return result;
    }

    // =========================================================================
    // MÉTODO PARA EL DASHBOARD 
    // =========================================================================
    public DashboardResponse getDashboardStats(User user) {
        List<Task> tasks = taskRepository.findByAuthor(user, Sort.by("createdAt").ascending());
        
        long total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        long pending = total - completed;
        
        long overdue = tasks.stream()
                .filter(t -> !t.isCompleted() && t.getDeadline() != null && t.getDeadline().isBefore(LocalDateTime.now()))
                .count();

        return DashboardResponse.builder()
                .totalTasks(total)
                .completedTasks(completed)
                .pendingTasks(pending)
                .overdueTasks(overdue)
                .build();
    }

        public List<Task> findByTitleAndUser(String title, User user) {
        List<Task> result = taskRepository.findByAuthorAndTitleContainingIgnoreCase(user, title);
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    public List<Task> findByCompletedAndUser(boolean completed, User user) {
        List<Task> result = taskRepository.findByAuthorAndCompleted(user, completed, Sort.by("createdAt").ascending());
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    public List<Task> findByCategoryNameAndUser(String categoryName, User user) {
        List<Task> result = taskRepository.findByAuthorAndCategory_TitleIgnoreCase(
                user, 
                categoryName, 
                Sort.by("createdAt").ascending()
        );
        if (result.isEmpty()) throw new EmptyTaskListException();
        return result;
    }

    // =========================================================================
    // MÉTODOS EXCLUSIVOS PARA ADMIN (Ignoran la validación de autoría)
    // =========================================================================

    public Task editTaskAdmin(EditTaskRequest req) {
        // Llama directamente a la edición sin pasar por getTaskIfAuthorized
        return createOrEditTask(req, null);
    }

    public void deleteByIdAdmin(Long id) {
        Task task = findById(id);
        taskRepository.delete(task);
    }

    public Task toggleCompleteAdmin(Long id) {
        Task task = findById(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }


}