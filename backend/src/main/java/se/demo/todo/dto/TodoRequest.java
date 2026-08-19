package se.demo.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import se.demo.todo.domain.Priority;
import se.demo.todo.domain.TodoStatus;

public record TodoRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank String description,
        @NotNull Priority priority,
        TodoStatus status
) {}
