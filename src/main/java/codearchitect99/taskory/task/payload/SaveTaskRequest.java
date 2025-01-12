package codearchitect99.taskory.task.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaveTaskRequest {
    @NotNull private String title;
    private Long eventId;
    @NotNull private Long tagId;
    private List<Long> hashtagIds;
    private String description;
    @ NotNull private String status;
    private List<TaskItemDto> items;
    private String deadline;
}
