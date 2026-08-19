package se.demo.todo.dto;

import se.demo.todo.domain.TodoStatus;
import java.time.LocalDateTime;

public record StatusHistoryResponse(
        Long id,
        TodoStatus fromStatus,
        String fromStatusText,
        TodoStatus toStatus,
        String toStatusText,
        LocalDateTime changedAt,
        String message
) {}
