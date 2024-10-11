package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.hashtag.HashtagResponse;
import codeartist99.taskflower.tag.payload.TagResponse;
import codeartist99.taskflower.task.model.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskResponse {
    private Long id;
    private String title;
    private EventSummary event;
    private TagResponse tag;
    private List<HashtagResponse> hashtags = new ArrayList<>();
    private String description;
    private String status;
    private List<TaskItemResponse> items = new ArrayList<>();

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        if (task.getEvent() != null) {
            this.event = new EventSummary(task.getEvent());
        }
        if (task.getTag() != null) {
            this.tag = new TagResponse(task.getTag());
        }
        if (task.getHashtags() != null && !task.getHashtags().isEmpty()) {
            this.hashtags = task.getHashtags().stream().map(HashtagResponse::new).toList();
        }
        this.description = task.getDescription();
        this.status = task.getStatus().name();
        if (task.getItems() != null && !task.getItems().isEmpty()) {
            this.items = task.getItems().stream().map(TaskItemResponse::new).toList();
        }
    }
}
