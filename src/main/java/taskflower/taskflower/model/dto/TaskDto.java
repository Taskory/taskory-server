package taskflower.taskflower.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import taskflower.taskflower.model.entity.Tag;
import taskflower.taskflower.model.enums.TaskStatus;

import java.util.Set;

@Data
public class TaskDto {
    private Long id;

    @NotNull
    private String title;

    private String description;

    private TaskStatus taskStatus;

    private Set<Tag> tags;

    private int[] startTime;

    private int[] endTime;

}
