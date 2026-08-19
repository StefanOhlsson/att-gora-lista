package se.demo.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.demo.todo.dto.StatusUpdateRequest;
import se.demo.todo.dto.TodoRequest;
import se.demo.todo.dto.TodoResponse;
import se.demo.todo.mapper.TodoMapper;
import se.demo.todo.service.TodoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService service;
    private final TodoMapper mapper;

    @GetMapping
    public List<TodoResponse> getAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public TodoResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoRequest request) {
        TodoResponse created = mapper.toResponse(service.create(request));
        return ResponseEntity.created(URI.create("/api/todos/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        return mapper.toResponse(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public TodoResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return mapper.toResponse(service.updateStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
