package codeartist99.taskflower.task;

import codeartist99.taskflower.setup.ArrangeTest;
import codeartist99.taskflower.task.service.TaskItemService;
import codeartist99.taskflower.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Deprecated
public class TaskAndTaskItemTest extends ArrangeTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskItemService taskItemService;

//    @Test
//    @DisplayName("is correct task item in correct task")
//    void saveTaskAndTaskItem() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
////        Arrange, Act
//        SaveTaskRequest saveTaskRequest = new SaveTaskRequest("test task title",  null, tempTag.getId(), null, null, "TO_DO");
//        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);
//
//        TaskItemDto taskItemDto = new TaskItemDto(taskResponse.getId(), "test task item title");
//        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, taskItemDto);
//
////        Assert
//        TaskResponse savedTask = taskService.getById(taskResponse.getId());
//        System.out.println(savedTask);
//        assertEquals(taskItemResponse, savedTask.getItems().get(0));
//    }
}
