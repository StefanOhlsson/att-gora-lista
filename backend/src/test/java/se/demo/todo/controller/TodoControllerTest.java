package se.demo.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.demo.todo.domain.Priority;
import se.demo.todo.domain.TodoItem;
import se.demo.todo.domain.TodoStatus;
import se.demo.todo.mapper.TodoMapper;
import se.demo.todo.service.TodoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService service;

    @MockBean
    TodoMapper mapper;

    @Test
    void shouldReturnTodos() throws Exception {
        TodoItem item = TodoItem.builder()
                .id(1L)
                .title("Testuppgift")
                .description("Beskrivning")
                .priority(Priority.HOG)
                .status(TodoStatus.REGISTRERAD)
                .createdAt(LocalDateTime.now())
                .build();

        when(service.findAll()).thenReturn(List.of(item));
        when(mapper.toResponse(item)).thenCallRealMethod();

        mockMvc.perform(get("/api/todos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Testuppgift"));
    }
}
