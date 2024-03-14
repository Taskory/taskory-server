package taskflower.taskflower.task;

import lombok.Data;
import taskflower.taskflower.task.tag.Tag;

import java.util.Set;

@Data
public class TaskDto {
    private String title;

    private String description;

    private Status status;

    private Set<Tag> tags;

    private int[] startTime;

    private int[] endTime;

}
