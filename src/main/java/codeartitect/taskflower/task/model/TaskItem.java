package codeartitect.taskflower.task.model;

import codeartitect.taskflower.task.payload.SaveTaskItemRequest;
import codeartitect.taskflower.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "title")
    private String title;

    @Column(name = "completed")
    private boolean completed = false;

    public TaskItem(User user, Task task, SaveTaskItemRequest saveTaskItemRequest) {
        this.user = user;
        this.task = task;
        this.title = saveTaskItemRequest.getTitle();
    }
}
