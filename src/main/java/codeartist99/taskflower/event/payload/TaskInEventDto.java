package codeartist99.taskflower.event.payload;

import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.model.Task;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInEventDto {
    private Long id;
    private String title;
    private Status status;

    public TaskInEventDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.status = task.getStatus();
    }
}
