package es.educastur.todo_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.educastur.todo_app.models.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByText(String text);
}
