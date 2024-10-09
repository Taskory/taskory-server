package codeartist99.taskflower.task.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveTaskRequest {
    private String title;
    private Long eventId;
    private Long tagId;
    private List<Long> hashtagIds;
    private String description;
    private String status;
}
