package codeartitect.taskflower.task.taskitem;

import codeartitect.taskflower.task.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskItemRequest {
    private Task task;
    private String title;
}
