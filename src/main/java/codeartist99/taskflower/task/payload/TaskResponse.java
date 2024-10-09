package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.model.TaskItem;
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
    private Event event;
    private Tag tag;
    private List<Hashtag> hashtags;
    private String description;
    private Status status;
    private List<TaskItemResponse> items;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.event = task.getEvent();
        this.tag = task.getTag();
        this.hashtags = task.getHashtags();
        this.description = task.getDescription();
        this.status = task.getStatus();
        setItems(task.getItems());
    }

    private void setItems(List<TaskItem> items) {
        if (items == null || items.isEmpty()) {
            this.items = new ArrayList<>();
        } else {
            this.items = new ArrayList<>();
            for (TaskItem item : items) {
                this.items.add(new TaskItemResponse(item));
            }
        }
    }
}
