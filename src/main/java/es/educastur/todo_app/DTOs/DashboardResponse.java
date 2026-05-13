package es.educastur.todo_app.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long overdueTasks; 
}