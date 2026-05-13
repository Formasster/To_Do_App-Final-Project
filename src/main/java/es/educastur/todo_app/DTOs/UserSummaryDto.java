package es.educastur.todo_app.DTOs;

import es.educastur.todo_app.models.User;

/**
 * JUSTIFICACIÓN DEL USO DE ESTE DTO:
 * * Se utiliza UserSummaryDto para transferir información pública o resumida de un usuario.
 * Su uso está justificado por dos motivos principales:
 * 1. Seguridad: Previene la exposición accidental de datos sensibles que existen en la 
 * entidad User (como la contraseña hasheada 'password' o el 'email').
 * 2. Rendimiento (Eficiencia): Al reducir el payload JSON a solo el 'id' y el 'username', 
 * se optimiza el ancho de banda, ideal para listados largos o cuando el usuario 
 * es solo un campo anidado dentro de otra entidad (ej. el autor de una Task).
 */
public record UserSummaryDto(
        Long id,
        String username
) {
    public static UserSummaryDto of(User user) {
        return new UserSummaryDto(
                user.getId(),
                user.getUsername()
        );
    }
}