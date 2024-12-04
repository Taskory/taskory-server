package codeartist99.taskflower.task.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveTaskRequest {
    @NotNull private String title;
    private Long eventId;
    @NotNull private Long tagId;
    private List<Long> hashtagIds;
    private String description;
    @ NotNull private String status;
    private List<TaskItemDto> items;
}
