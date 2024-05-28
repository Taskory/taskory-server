package codeartitect.taskflower.task;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.HashTag;
import codeartitect.taskflower.user.entity.User;

import java.util.Set;

public class TaskResponse {
    private Long id;
    private String title;
    private Flow flow;
    private Event event;
    private Tag tag;
    private Set<HashTag> hashTags;
    private String description;
    private Status status;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.flow = task.getFlow();
        this.event = task.getEvent();
        this.tag = task.getTag();
        this.hashTags = task.getHashTags();
        this.description = task.getDescription();
        this.status = task.getStatus();
    }
}
