package es.educastur.todo_app.DTOs;

import es.educastur.todo_app.models.Tag;
import es.educastur.todo_app.models.Task;
import es.educastur.todo_app.models.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EditTaskRequest extends CreateTaskRequest{

    private Long id;
    private boolean completed;
    private LocalDateTime createdAt;
    private String username;
    private TaskStatus status;


    public static EditTaskRequest of(Task task) {
        return EditTaskRequest.builder()
                .id(task.getId())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .username(task.getAuthor().getFullname())
                .title(task.getTitle())
                .description(task.getDescription())
                .categoryId(task.getCategory().getId())
                .tags(task.getTags().stream().map(Tag::getText).collect(Collectors.joining(", ")))
                .deadline(task.getDeadline())
                .priority(task.getPriority())
                .status(task.getStatus())
                .starred(task.isStarred())
                .notes(task.getNotes())
                .build();
    }

}
