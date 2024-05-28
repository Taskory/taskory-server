package codeartitect.taskflower.task;

import codeartitect.taskflower.Tag.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.HashTag;
import lombok.Getter;

import java.util.Set;

@Getter
public class SaveTaskRequest {
    private String title;
    private Flow flow;
    private Event event;
    private Tag tag;
    private Set<HashTag> hashTags;
    private String description;
    private Status status;
}
