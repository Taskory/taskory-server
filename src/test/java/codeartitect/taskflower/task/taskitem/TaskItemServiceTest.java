package codeartitect.taskflower.task.taskitem;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.model.Status;
import codeartitect.taskflower.task.model.Task;
import codeartitect.taskflower.task.exception.TaskItemNotFoundException;
import codeartitect.taskflower.task.payload.SaveTaskItemRequest;
import codeartitect.taskflower.task.payload.TaskItemResponse;
import codeartitect.taskflower.task.repository.TaskRepository;
import codeartitect.taskflower.task.service.TaskItemService;
import codeartitect.taskflower.user.model.Role;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskItemServiceTest {
    @Autowired
    private TaskItemService taskItemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;
    private User user;

    private Task task;

    @BeforeEach
    void setUp() {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        String username = tempUsername.toString();
        String password = "1234";
        String zoneId = "Asia/Seoul";
        user = new User(null, username, password, zoneId, null, Collections.singletonList(Role.USER));
        userRepository.save(user);

        String title = "test title";
        Flow flow = null;
        Event event = null;
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        task = new Task(null, user, title, flow, event, tag, hashtags, description, Status.TODO, null);
        taskRepository.save(task);

    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving task item
     */
    @Test
    @DisplayName("save task item and get flow test")
    void save() {
//        Arrange
        String title = "test tile";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(task.getId(), title);

//        Act
        TaskItemResponse taskItemResponse = taskItemService.save(user, saveTaskItemRequest);

//        Assert
        assertEquals(taskItemResponse, taskItemService.getById(taskItemResponse.getId()));
    }

    /**
     * Test for find all task items
     */
    @Test
    @DisplayName("find all task items by task")
    void findAllByTaskId() {
//        Arrange
        String title = "test tile";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(task.getId(), title);

        String title2 = "test title2";
        SaveTaskItemRequest saveTaskItemRequest2 = new SaveTaskItemRequest(task.getId(), title2);

        TaskItemResponse taskItemResponse = taskItemService.save(user, saveTaskItemRequest);
        TaskItemResponse taskItemResponse2 = taskItemService.save(user, saveTaskItemRequest2);

//        Act
        List<TaskItemResponse> items = taskItemService.findAllByTaskId(task.getId());

//        Assert
        assertEquals(taskItemResponse, items.get(0));
        assertEquals(taskItemResponse2, items.get(1));

    }

    /**
     * Update task item tile
     */
    @Test
    @DisplayName("update task item title")
    void updateTitle() {
//        Arrange
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(task.getId(), title);
        TaskItemResponse item = taskItemService.save(user, saveTaskItemRequest);

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
    void setCompleted() {
//        Arrange
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(task.getId(), title);
        TaskItemResponse item = taskItemService.save(user, saveTaskItemRequest);

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
    void deleteById() {
//        Arrange
//        save a task item
        String title = "test title";
        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(task.getId(), title);

        TaskItemResponse itemResponse = taskItemService.save(user, saveTaskItemRequest);

        Long itemId = itemResponse.getId();

//        Act
        taskItemService.deleteById(itemResponse.getId());

//        Assert
        assertThrows(TaskItemNotFoundException.class, () -> taskItemService.getById(itemId));
    }

}