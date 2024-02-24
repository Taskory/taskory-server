package taskflower.taskflower.task;

import jakarta.persistence.*;
import lombok.Data;
import taskflower.taskflower.user.User;

import java.time.ZonedDateTime;

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

    @Column(nullable = false)
    @ManyToOne
    private User email;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String tag;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

    @Column(nullable = false)
    private ZonedDateTime createdTime;

    @Column(nullable = false)
    private ZonedDateTime modifiedTime;
}
