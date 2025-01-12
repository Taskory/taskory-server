package codearchitect99.taskory.task.payload;

import codearchitect99.taskory.event.payload.EventSummary;
import codearchitect99.taskory.hashtag.HashtagResponse;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.task.model.TaskItem;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TaskSummary {
    @NotNull private Long id;
    @NotNull private String title;
    private EventSummary event;
    @NotNull private String tagTitle;
    @NotNull private String tagColor;
    private List<HashtagResponse> hashtags;
    @NotNull private String status;
    @NotNull private Integer itemsCount;
    private Integer completedItemsCount;
    private String deadline;

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
        this.itemsCount = task.getItems().size();
        this.completedItemsCount = (int) task.getItems().stream()
                .filter(TaskItem::isCompleted)
                .count();
        if (task.getDeadline() != null) {
            this.deadline = task.getDeadline().toString();
        }
    }
}
