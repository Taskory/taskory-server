package codeartist99.taskflower.task.payload;

import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.hashtag.HashtagResponse;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.model.TaskItem;
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
    @NotNull private Integer progressRate;

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
        this.progressRate = calculateProgress(task.getItems());
    }

    private static Integer calculateProgress(List<TaskItem> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }

        long completedItems = items.stream()
                .filter(TaskItem::isCompleted)
                .count();

        return Math.round((float) completedItems / items.size() * 100);
    }
}
