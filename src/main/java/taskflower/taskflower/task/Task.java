package taskflower.taskflower.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import taskflower.taskflower.user.User;

import java.time.LocalDateTime;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @ManyToOne
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String tag;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    public Task() {

    }
}
