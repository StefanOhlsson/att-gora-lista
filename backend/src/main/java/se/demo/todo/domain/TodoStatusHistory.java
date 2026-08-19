package se.demo.todo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todo_status_history")
public class TodoStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_item_id")
    private TodoItem todoItem;

    @Enumerated(EnumType.STRING)
    private TodoStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus toStatus;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(nullable = false, length = 255)
    private String message;
}
