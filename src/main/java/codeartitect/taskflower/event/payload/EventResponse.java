package codeartitect.taskflower.event.payload;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.hashtag.Hashtag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class EventResponse {
    private Long id;
    private String title;
    private Tag tag;
    private List<Hashtag> hashtags;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    private String location;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.tag = event.getTag();
        this.hashtags = event.getHashtags();
        this.description = event.getDescription();
        this.startDateTime = event.getStartDateTime();
        this.dueDateTime = event.getDueDateTime();
        this.location = event.getLocation();
    }
}
