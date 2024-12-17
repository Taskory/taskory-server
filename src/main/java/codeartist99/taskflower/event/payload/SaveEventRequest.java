package codeartist99.taskflower.event.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SaveEventRequest {
    @NotNull(message = "Title cannot be null.")
    private String title;
    @NotNull(message = "Tag id cannot be null.")
    private Long tagId;
    @NotNull(message = "Must not be null. use an empty list if applicable.")
    private List<TaskInEventDto> tasks = new ArrayList<>();
    @NotNull(message = "Must not be null. use an empty list if applicable.")
    private List<Long> hashtagIds = new ArrayList<>();
    private String description;
    @NotNull(message = "Start date time cannot be null.")
    private String startDateTime;
    @NotNull(message = "Due date time cannot be null.")
    private String dueDateTime;
    private String location;
}
