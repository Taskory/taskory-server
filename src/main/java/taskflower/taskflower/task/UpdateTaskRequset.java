package taskflower.taskflower.task;

import lombok.Data;

@Data
public class UpdateTaskRequset {
    private String title;

    private String description;

    private Status status;

    private String tag;

    private String startTime;           // "yyyy-mm-ddThh:mm:ss Asia/Seoul"

    private String endTime;

}
