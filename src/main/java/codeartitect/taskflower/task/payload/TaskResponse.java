package codeartitect.taskflower.task.payload;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.model.Status;
import codeartitect.taskflower.task.model.Task;
import codeartitect.taskflower.task.model.TaskItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
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
    private List<Hashtag> hashtags;
    private String description;
    private Status status;
    private List<TaskItemResponse> items;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.flow = task.getFlow();
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
