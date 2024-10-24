package codeartist99.taskflower.task.model;

import codeartist99.taskflower.task.payload.SaveTaskItemRequest;
import codeartist99.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "TaskItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)          // If a user is deleted, the mapped task items are also deleted
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "completed")
    private boolean completed = false;

    public TaskItem(User user, Task task, SaveTaskItemRequest saveTaskItemRequest) {
        this.user = user;
        this.task = task;
        this.title = saveTaskItemRequest.getTitle();
    }
}
