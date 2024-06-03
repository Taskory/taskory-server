package codeartitect.taskflower.task.taskitem;

import codeartitect.taskflower.task.Task;
import codeartitect.taskflower.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column
    private String title;

    @Column
    private boolean completed = false;

    public TaskItem(User user, Task task, SaveTaskItemRequest saveTaskItemRequest) {
        this.user = user;
        this.task = task;
        this.title = saveTaskItemRequest.getTitle();
    }
}
