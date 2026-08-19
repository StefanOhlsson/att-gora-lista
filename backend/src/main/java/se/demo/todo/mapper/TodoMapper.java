package se.demo.todo.mapper;

import org.springframework.stereotype.Component;
import se.demo.todo.domain.TodoItem;
import se.demo.todo.domain.TodoStatusHistory;
import se.demo.todo.dto.StatusHistoryResponse;
import se.demo.todo.dto.TodoResponse;

@Component
public class TodoMapper {
    public TodoResponse toResponse(TodoItem item) {
        return new TodoResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPriority(),
                item.getPriority().getLabel(),
                item.getStatus(),
                item.getStatus().getLabel(),
                item.getCreatedAt(),
                item.getHistory().stream().map(this::toHistoryResponse).toList()
        );
    }

    private StatusHistoryResponse toHistoryResponse(TodoStatusHistory history) {
        return new StatusHistoryResponse(
                history.getId(),
                history.getFromStatus(),
                history.getFromStatus() == null ? null : history.getFromStatus().getLabel(),
                history.getToStatus(),
                history.getToStatus().getLabel(),
                history.getChangedAt(),
                history.getMessage()
        );
    }
}
