package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.hashtag.HashtagResponse;
import codeartist99.taskflower.tag.payload.TagResponse;
import codeartist99.taskflower.task.model.Task;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskResponse {
    @NotNull private Long id;
    @NotNull private String title;
    private EventSummary event;
    @NotNull private TagResponse tag;
    private List<HashtagResponse> hashtags = new ArrayList<>();
    private String description;
    @NotNull private String status;
    private List<TaskItemDto> items = new ArrayList<>();
    private String deadline;

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
        if (task.getDescription() != null) {
            this.description = task.getDescription();
        } else this.description = "";
        this.status = task.getStatus().name();

        if (task.getItems() != null && !task.getItems().isEmpty()) {
            this.items = task.getItems().stream().map(TaskItemDto::new).toList();
        }
        if (task.getDeadline() != null) {
            this.deadline = task.getDeadline().toString();
        }
    }
}
