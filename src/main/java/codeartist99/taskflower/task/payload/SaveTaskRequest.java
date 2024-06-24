package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.flow.Flow;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.task.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveTaskRequest {
    private String title;
    private Flow flow;
    private Event event;
    private Tag tag;
    private List<Hashtag> hashtags;
    private String description;
    private Status status;
}
