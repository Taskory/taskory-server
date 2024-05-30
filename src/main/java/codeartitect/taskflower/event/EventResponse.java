package codeartitect.taskflower.event;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
public class EventResponse {
    private Long id;
    private String title;
    private Tag tag;
    private Set<Hashtag> hashtags;
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
