package taskflower.taskflower.task;

import lombok.Data;

@Data
public class SaveTaskRequset {
    private String title;

    private String description;

    private Status status;

    private String tag;

    private int[] startTime;

    private int[] endTime;

}
