package taskflower.taskflower.task;

import jakarta.persistence.*;
import lombok.Data;
import taskflower.taskflower.task.tag.Tag;
import taskflower.taskflower.user.User;

import java.time.LocalDateTime;
import java.util.Set;

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

//    단방향 참조
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;
}
