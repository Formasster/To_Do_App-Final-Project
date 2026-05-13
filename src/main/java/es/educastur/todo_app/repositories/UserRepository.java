package es.educastur.todo_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.educastur.todo_app.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);
    
    // método estrictamente para Spring Security
    Optional<User> findByUsername(String username); 

}