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
    private ZonedDateTime updatedTime;

    public Task(SaveTaskRequset saveTaskRequset) {
        this.title = saveTaskRequset.getTitle();
        this.description = saveTaskRequset.getDescription();
        this.status = saveTaskRequset.getStatus();
        this.tag = saveTaskRequset.getTag();
        this.startTime = saveTaskRequset.getStartTime();
        this.endTime = saveTaskRequset.getEndTime();
        this.createdTime = ZonedDateTime.now();
        this.updatedTime = ZonedDateTime.now();
    }

    public Task() {
    }

    public Task(UpdateTaskRequset updateTaskRequset) {
        this.title = updateTaskRequset.getTitle();
        this.description = updateTaskRequset.getDescription();
        this.status = updateTaskRequset.getStatus();
        this.tag = updateTaskRequset.getTag();
        this.startTime = updateTaskRequset.getStartTime();
        this.endTime = updateTaskRequset.getEndTime();
        this.updatedTime = ZonedDateTime.now();
    }
}
