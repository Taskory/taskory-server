package codeartist99.taskflower.task;

import codeartist99.taskflower.setup.ArrangeTest;
import codeartist99.taskflower.task.service.TaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Deprecated
class TaskItemServiceTest extends ArrangeTest {
    @Autowired
    private TaskItemService taskItemService;

    /**
     * @deprecated
     * Test for saving task item
     */
//    @Test
//    @DisplayName("save task item and get flow test")
//    void save() throws TaskNotFoundException, TaskItemNotFoundException {
////        Arrange
//        String title = "test tile";
//        TaskItemDto taskItemDto = new TaskItemDto(tempTask.getId(), title);
//
////        Act
//        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, taskItemDto);
//
////        Assert
//        assertEquals(taskItemResponse, taskItemService.getById(taskItemResponse.getId()));
//    }

    /**
     * @deprecated
     * Test for find all task items
     */
//    @Test
//    @DisplayName("find all task items by task")
//    void findAllByTaskId() throws TaskNotFoundException {
////        Arrange
//        String title = "test tile";
//        TaskItemDto taskItemDto = new TaskItemDto(tempTask.getId(), title);
//
//        String title2 = "test title2";
//        TaskItemDto taskItemDto2 = new TaskItemDto(tempTask.getId(), title2);
//
//        TaskItemResponse taskItemResponse = taskItemService.save(tempUser, taskItemDto);
//        TaskItemResponse taskItemResponse2 = taskItemService.save(tempUser, taskItemDto2);
//
////        Act
//        List<TaskItemResponse> items = taskItemService.findAllByTaskId(tempTask.getId());
//
////        Assert
//        assertEquals(taskItemResponse, items.get(0));
//        assertEquals(taskItemResponse2, items.get(1));
//
//    }

    /**
     * @deprecated
     * Update task item tile
     */
//    @Test
//    @DisplayName("update task item title")
//    void updateTitle() throws TaskNotFoundException, TaskItemNotFoundException {
////        Arrange
//        String title = "test title";
//        TaskItemDto taskItemDto = new TaskItemDto(tempTask.getId(), title);
//        TaskItemResponse item = taskItemService.save(tempUser, taskItemDto);
//
////        Act
//        String updateTitle = "update title";
//        TaskItemResponse updateItemResponse = taskItemService.updateTitle(item.getId(), updateTitle);
//
////        Assert
//        assertEquals(updateItemResponse, taskItemService.getById(item.getId()));
//    }

    /**
     * @deprecated
     * Modify completed status of task item
     */
//    @Test
//    @DisplayName("set completed of task item")
//    void setCompleted() throws TaskNotFoundException, TaskItemNotFoundException {
////        Arrange
//        String title = "test title";
//        TaskItemDto taskItemDto = new TaskItemDto(tempTask.getId(), title);
//        TaskItemResponse item = taskItemService.save(tempUser, taskItemDto);
//
////        Act
////        default completed status of task item is false, so set True
//        TaskItemResponse updateItemResponse = taskItemService.setCompleted(item.getId(), true);
//
////        Assert
//        assertEquals(updateItemResponse, taskItemService.getById(item.getId()));
//    }

    /**
     * @deprecated
     * Test for delete a task item
     */
//    @Test
//    @DisplayName("delete task item")
//    void deleteById() throws TaskNotFoundException, TaskItemNotFoundException {
////        Arrange
////        save a task item
//        String title = "test title";
//        TaskItemDto taskItemDto = new TaskItemDto(tempTask.getId(), title);
//
//        TaskItemResponse itemResponse = taskItemService.save(tempUser, taskItemDto);
//
//        Long itemId = itemResponse.getId();
//
////        Act
//        taskItemService.deleteById(itemResponse.getId());
//
////        Assert
//        assertThrows(TaskItemNotFoundException.class, () -> taskItemService.getById(itemId));
//    }

}