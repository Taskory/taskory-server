package codeartist99.taskflower.task;

import codeartist99.taskflower.setup.ArrangeTest;
import codeartist99.taskflower.task.exception.TaskItemNotFoundException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.payload.SaveTaskItemRequest;
import codeartist99.taskflower.task.payload.TaskItemResponse;
import codeartist99.taskflower.task.service.TaskItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TaskItemServiceTest extends ArrangeTest {
    @Autowired
    private TaskItemService taskItemService;

    /**
     * Test for saving task item
     */
    @Test
    @DisplayName("save task item and get flow test")
    void save() throws TaskNotFoundException, TaskItemNotFoundException {
//        Arrange
        String title = "test tile";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(tempTask.getId(), title);

//        Act
        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, saveTaskItemRequest);

//        Assert
        assertEquals(taskItemResponse, taskItemService.getById(taskItemResponse.getId()));
    }

    /**
     * Test for find all task items
     */
    @Test
    @DisplayName("find all task items by task")
    void findAllByTaskId() throws TaskNotFoundException {
//        Arrange
        String title = "test tile";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(tempTask.getId(), title);

        String title2 = "test title2";
        SaveTaskItemRequest saveTaskItemRequest2 = new SaveTaskItemRequest(tempTask.getId(), title2);

        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, saveTaskItemRequest);
        TaskItemResponse taskItemResponse2 = taskItemService.save(tempUser, saveTaskItemRequest2);

//        Act
        List<TaskItemResponse> items = taskItemService.findAllByTaskId(tempTask.getId());

//        Assert
        assertEquals(taskItemResponse, items.get(0));
        assertEquals(taskItemResponse2, items.get(1));

    }

    /**
     * Update task item tile
     */
    @Test
    @DisplayName("update task item title")
    void updateTitle() throws TaskNotFoundException, TaskItemNotFoundException {
//        Arrange
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(tempTask.getId(), title);
        TaskItemResponse item = taskItemService.save(tempUser, saveTaskItemRequest);

//        Act
        String updateTitle = "update title";
        TaskItemResponse updateItemResponse = taskItemService.updateTitle(item.getId(), updateTitle);

//        Assert
        assertEquals(updateItemResponse, taskItemService.getById(item.getId()));
    }

    /**
     * Modify completed status of task item
     */
    @Test
    @DisplayName("set completed of task item")
    void setCompleted() throws TaskNotFoundException, TaskItemNotFoundException {
//        Arrange
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(tempTask.getId(), title);
        TaskItemResponse item = taskItemService.save(tempUser, saveTaskItemRequest);

//        Act
//        default completed status of task item is false, so set True
        TaskItemResponse updateItemResponse = taskItemService.setCompleted(item.getId(), true);

//        Assert
        assertEquals(updateItemResponse, taskItemService.getById(item.getId()));
    }

    /**
     * Test for delete a task item
     */
    @Test
    @DisplayName("delete task item")
    void deleteById() throws TaskNotFoundException, TaskItemNotFoundException {
//        Arrange
//        save a task item
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(tempTask.getId(), title);

        TaskItemResponse itemResponse = taskItemService.save(tempUser, saveTaskItemRequest);

        Long itemId = itemResponse.getId();

//        Act
        taskItemService.deleteById(itemResponse.getId());

//        Assert
        assertThrows(TaskItemNotFoundException.class, () -> taskItemService.getById(itemId));
    }

}