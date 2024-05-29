package codeartitect.taskflower.task;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.Hashtag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class TaskResponse {
    private Long id;
    private String title;
    private Flow flow;
    private Event event;
    private Tag tag;
    private Set<Hashtag> hashtags;
    private String description;
    private Status status;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.flow = task.getFlow();
        this.event = task.getEvent();
        this.tag = task.getTag();
        this.hashtags = task.getHashtags();
        this.description = task.getDescription();
        this.status = task.getStatus();
    }
}
