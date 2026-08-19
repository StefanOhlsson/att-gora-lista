package se.demo.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.demo.todo.domain.TodoItem;
import se.demo.todo.domain.TodoStatus;
import se.demo.todo.domain.TodoStatusHistory;
import se.demo.todo.dto.TodoRequest;
import se.demo.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;

    @Transactional(readOnly = true)
    public List<TodoItem> findAll() {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public TodoItem findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Uppgiften finns inte: " + id));
    }

    @Transactional
    public TodoItem create(TodoRequest request) {
        TodoStatus status = request.status() == null ? TodoStatus.REGISTRERAD : request.status();
        TodoItem item = TodoItem.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
        item.addHistory(history(null, status, "Uppgift registrerad med status " + status.getLabel()));
        return repository.save(item);
    }

    @Transactional
    public TodoItem update(Long id, TodoRequest request) {
        TodoItem item = findById(id);
        item.setTitle(request.title());
        item.setDescription(request.description());
        item.setPriority(request.priority());
        if (request.status() != null && request.status() != item.getStatus()) {
            changeStatus(item, request.status());
        }
        return repository.save(item);
    }

    @Transactional
    public TodoItem updateStatus(Long id, TodoStatus newStatus) {
        TodoItem item = findById(id);
        if (item.getStatus() != newStatus) {
            changeStatus(item, newStatus);
        }
        return repository.save(item);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void changeStatus(TodoItem item, TodoStatus newStatus) {
        TodoStatus oldStatus = item.getStatus();
        item.setStatus(newStatus);
        item.addHistory(history(oldStatus, newStatus, "Status ändrad från " + oldStatus.getLabel() + " till " + newStatus.getLabel()));
    }

    private TodoStatusHistory history(TodoStatus from, TodoStatus to, String message) {
        return TodoStatusHistory.builder()
                .fromStatus(from)
                .toStatus(to)
                .changedAt(LocalDateTime.now())
                .message(message)
                .build();
    }
}
