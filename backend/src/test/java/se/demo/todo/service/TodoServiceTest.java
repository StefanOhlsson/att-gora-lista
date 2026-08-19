package se.demo.todo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.demo.todo.domain.Priority;
import se.demo.todo.domain.TodoItem;
import se.demo.todo.domain.TodoStatus;
import se.demo.todo.dto.TodoRequest;
import se.demo.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    TodoRepository repository;

    @InjectMocks
    TodoService service;

    @Test
    void shouldCreateStatusHistoryWhenStatusChanges() {
        TodoItem item = TodoItem.builder()
                .id(1L)
                .title("Test")
                .description("Beskrivning")
                .priority(Priority.HOG)
                .status(TodoStatus.REGISTRERAD)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any(TodoItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoItem result = service.updateStatus(1L, TodoStatus.STARTAD);

        assertThat(result.getStatus()).isEqualTo(TodoStatus.STARTAD);
        assertThat(result.getHistory()).hasSize(1);
        assertThat(result.getHistory().getFirst().getMessage()).contains("Status ändrad från Registrerad till Startad");
    }

    @Test
    void shouldCreateNewTodoWithInitialHistory() {
        when(repository.save(any(TodoItem.class))).thenAnswer(invocation -> {
            TodoItem item = invocation.getArgument(0);
            item.setId(1L);
            return item;
        });

        TodoItem result = service.create(new TodoRequest("Ny uppgift", "Beskrivning", Priority.NORMAL, null));

        assertThat(result.getStatus()).isEqualTo(TodoStatus.REGISTRERAD);
        assertThat(result.getHistory()).hasSize(1);
    }
}
