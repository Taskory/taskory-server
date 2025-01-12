package codearchitect99.taskory.event.payload;

import codearchitect99.taskory.task.model.Status;
import codearchitect99.taskory.task.model.Task;
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
