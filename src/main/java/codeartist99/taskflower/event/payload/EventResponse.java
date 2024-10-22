package codeartist99.taskflower.event.payload;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.hashtag.HashtagResponse;
import codeartist99.taskflower.tag.payload.TagResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EventResponse {
    private Long id;
    private String title;
    private TagResponse tag;
    private List<HashtagResponse> hashtags = new ArrayList<>();
    private String description;
    private String startDateTime;
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
        if (event.getHashtags() != null && !event.getHashtags().isEmpty()) {
            this.hashtags = event.getHashtags().stream().map(HashtagResponse::new).toList();
        }
        this.description = event.getDescription();
        this.startDateTime = event.getStartDateTime().toString();
        this.dueDateTime = event.getDueDateTime().toString();
        this.location = event.getLocation();
    }
}
