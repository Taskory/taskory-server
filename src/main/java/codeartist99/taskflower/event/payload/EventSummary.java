package codeartist99.taskflower.event.payload;

import codeartist99.taskflower.event.Event;

import java.time.LocalDateTime;

public class EventSummary {
    private Long id;
    private String title;
    private String tagTitle;
    private String tagColor;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;

    public EventSummary(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        if (event.getTag() != null) {
            this.tagTitle = event.getTag().getTitle();
            this.tagColor = event.getTag().getColor().toString();
        } else {
            this.tagTitle = "";
            this.tagColor = "";
        }
        this.startDateTime = event.getStartDateTime();
        this.dueDateTime = event.getDueDateTime();
    }
}
