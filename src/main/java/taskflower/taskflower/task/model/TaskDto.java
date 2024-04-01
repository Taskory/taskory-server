package taskflower.taskflower.task.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import taskflower.taskflower.task.tag.model.Tag;

import java.util.Set;

@Data
public class TaskDto {
    private Long id;

    @NotNull
    private String title;

    private String description;

    private Status status;

    private Set<Tag> tags;

    private int[] startTime;

    private int[] endTime;

}
