package es.educastur.todo_app.task.model;

import es.educastur.todo_app.category.model.Category;
import es.educastur.todo_app.user.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor(User user, Sort sort);
    List<Task> findByCategory(Category category);

}
