package codeartitect.taskflower.task;

import codeartitect.taskflower.task.taskitem.SaveTaskItemRequest;
import codeartitect.taskflower.task.taskitem.TaskItemResponse;
import codeartitect.taskflower.task.taskitem.TaskItemService;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskAndTaskItemTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskItemService taskItemService;

    @Autowired
    private UserService userService;

    private User user;

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
        user = new User(null, username, password, zoneId, null);
        userRepository.save(user);
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    @Test
    @DisplayName("is correct task item in correct task")
    void saveTaskAndTaskItem() {
//        Arrange, Act
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest("test task title", null, null, null, null, null, Status.TODO);
        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);

        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(taskResponse.getId(), "test task item title");
        TaskItemResponse taskItemResponse = taskItemService.save(user, saveTaskItemRequest);

//        Assert
        TaskResponse savedTask = taskService.getById(taskResponse.getId());
        System.out.println(savedTask);
        assertEquals(taskItemResponse, savedTask.getItems().get(0));
    }
}
