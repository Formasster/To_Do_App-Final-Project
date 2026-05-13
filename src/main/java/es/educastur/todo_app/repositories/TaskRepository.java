package es.educastur.todo_app.repositories;

import es.educastur.todo_app.models.Category;
import es.educastur.todo_app.models.Priority;
import es.educastur.todo_app.models.Task;
import es.educastur.todo_app.models.TaskStatus;
import es.educastur.todo_app.models.User;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor(User user, Sort sort);
    List<Task> findByCategory(Category category);
    List<Task> findByAuthorAndStarred(User author, boolean starred, Sort sort);
    List<Task> findByAuthorAndPriority(User author, Priority priority, Sort sort);
    List<Task> findByAuthorAndStatus(User author, TaskStatus status, Sort sort);
    List<Task> findByAuthor_Id(Long userId, Sort sort);
    List<Task> findByAuthorAndTags_Id(User author, Long tagId);
    List<Task> findByAuthorAndTitleContainingIgnoreCase(User author, String title);
    List<Task> findByAuthorAndCompleted(User author, boolean completed, Sort sort);
    List<Task> findByAuthorAndCategory_TitleIgnoreCase(User author, String categoryTitle, Sort sort);
}