package se.demo.todo.dto;

import se.demo.todo.domain.Priority;
import se.demo.todo.domain.TodoStatus;
import java.time.LocalDateTime;
import java.util.List;

public record TodoResponse(
        Long id,
        String title,
        String description,
        Priority priority,
        String priorityText,
        TodoStatus status,
        String statusText,
        LocalDateTime createdAt,
        List<StatusHistoryResponse> history
) {}
