package codeartist99.taskflower.task;

import codeartist99.taskflower.event.EventNotFoundException;
import codeartist99.taskflower.setup.ArrangeTest;
import codeartist99.taskflower.tag.TagNotFoundException;
import codeartist99.taskflower.task.exception.InvalidStatusNameException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.payload.SaveTaskItemRequest;
import codeartist99.taskflower.task.payload.SaveTaskRequest;
import codeartist99.taskflower.task.payload.TaskItemResponse;
import codeartist99.taskflower.task.payload.TaskResponse;
import codeartist99.taskflower.task.service.TaskItemService;
import codeartist99.taskflower.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskAndTaskItemTest extends ArrangeTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskItemService taskItemService;

    @Test
    @DisplayName("is correct task item in correct task")
    void saveTaskAndTaskItem() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
//        Arrange, Act
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest("test task title",  null, tempTag.getId(), null, null, "TO_DO");
        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);

        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(taskResponse.getId(), "test task item title");
        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, saveTaskItemRequest);

//        Assert
        TaskResponse savedTask = taskService.getById(taskResponse.getId());
        System.out.println(savedTask);
        assertEquals(taskItemResponse, savedTask.getItems().get(0));
    }
}
