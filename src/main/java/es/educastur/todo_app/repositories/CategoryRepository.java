package es.educastur.todo_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.educastur.todo_app.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
