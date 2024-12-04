package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.task.model.TaskItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskItemDto {
    private Long id;
    @NotNull private Long taskId;
    @NotNull private String title;
    @NotNull private boolean completed;

    public TaskItemDto(TaskItem taskItem) {
        this.id = taskItem.getId();
        this.taskId = taskItem.getTask().getId();
        this.title = taskItem.getTitle();
        this.completed = taskItem.isCompleted();
    }
}
