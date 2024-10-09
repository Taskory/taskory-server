package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.hashtag.HashtagResponse;
import codeartist99.taskflower.task.model.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TaskSummary {
    private Long id;
    private String title;
    private EventSummary event;
    private String tagTitle;
    private String tagColor;
    private List<HashtagResponse> hashtags;
    private String status;

    public TaskSummary(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.event = (task.getEvent() != null) ? new EventSummary(task.getEvent()) : null;
        if (task.getTag() != null) {
            this.tagTitle = task.getTag().getTitle();
            this.tagColor = task.getTag().getColor().name();
        }
        this.hashtags = task.getHashtags().stream().map(HashtagResponse::new).toList();
        this.status = task.getStatus().name();
    }
}
