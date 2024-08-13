package codeartist99.taskflower.event.payload;

import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.tag.model.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveEventRequest {
    @NotNull(message = "Title cannot be null.")
    private String title;
    private Long tagId;
    @NotNull(message = "Must not be null. use an empty list if applicable.")
    private List<Long> hashtagIds = new ArrayList<>();
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    private String location;
}
