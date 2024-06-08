package codeartitect.taskflower.task.payload;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveTaskRequest {
    private String title;
    private Flow flow;
    private Event event;
    private Tag tag;
    private Set<Hashtag> hashtags;
    private String description;
    private Status status;
}
