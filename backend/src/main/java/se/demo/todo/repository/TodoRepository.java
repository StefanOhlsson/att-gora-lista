package se.demo.todo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.demo.todo.domain.TodoItem;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {

    @Override
    @EntityGraph(attributePaths = "history")
    List<TodoItem> findAll();

    @Override
    @EntityGraph(attributePaths = "history")
    Optional<TodoItem> findById(Long id);
}