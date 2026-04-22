package es.educastur.todo_app.user.dto;


import es.educastur.todo_app.user.model.User;

public record UserResponse(
        Long id,
        String username,
        String fullname,
        String email,
        String role
) {

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
