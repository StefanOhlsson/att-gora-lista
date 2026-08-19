package se.demo.todo.dto;

import jakarta.validation.constraints.NotNull;
import se.demo.todo.domain.TodoStatus;

public record StatusUpdateRequest(@NotNull TodoStatus status) {}
