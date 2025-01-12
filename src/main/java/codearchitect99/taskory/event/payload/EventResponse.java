package codearchitect99.taskory.event.payload;

import codearchitect99.taskory.event.Event;
import codearchitect99.taskory.hashtag.HashtagResponse;
import codearchitect99.taskory.tag.payload.TagResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EventResponse {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull(message = "Title cannot be null.")
    private String title;
    @NotNull(message = "Tag cannot be null")
    private TagResponse tag;
    @NotNull(message = "Must not be null. use an empty list if applicable.")
    private List<TaskInEventDto> tasks = new ArrayList<>();
    @NotNull(message = "Must not be null. use an empty list if applicable.")
    private List<HashtagResponse> hashtags = new ArrayList<>();
    private String description;
    @NotNull(message = "Start date time cannot be null.")
    private String startDateTime;
    @NotNull(message = "Due date time cannot be null.")
    private String dueDateTime;
    private String location;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        if (event.getTag() != null) {
            this.tag = new TagResponse(event.getTag());
        } else {
            this.tag = null;
        }
        if (event.getTasks() != null && !event.getTasks().isEmpty()) {
            this.tasks = event.getTasks().stream().map(TaskInEventDto::new).toList();
        }
        if (event.getHashtags() != null && !event.getHashtags().isEmpty()) {
            this.hashtags = event.getHashtags().stream().map(HashtagResponse::new).toList();
        }
        this.description = event.getDescription();
        this.startDateTime = event.getStartDateTime().toString();
        this.dueDateTime = event.getDueDateTime().toString();
        this.location = event.getLocation();
    }
}
