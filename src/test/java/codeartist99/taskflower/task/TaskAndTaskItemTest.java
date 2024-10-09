package codeartist99.taskflower.task;

import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.payload.SaveTaskItemRequest;
import codeartist99.taskflower.task.payload.SaveTaskRequest;
import codeartist99.taskflower.task.payload.TaskItemResponse;
import codeartist99.taskflower.task.payload.TaskResponse;
import codeartist99.taskflower.task.service.TaskItemService;
import codeartist99.taskflower.task.service.TaskService;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.UserService;
import codeartist99.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        user = User.builder()
                .username(username)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    @Test
    @DisplayName("is correct task item in correct task")
    void saveTaskAndTaskItem() throws TaskNotFoundException {
//        Arrange, Act
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest("test task title",  null, null, null, null, Status.TODO);
        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);

        SaveTaskItemRequest saveTaskItemRequest = new SaveTaskItemRequest(taskResponse.getId(), "test task item title");
        TaskItemResponse taskItemResponse = taskItemService.save(user, saveTaskItemRequest);

//        Assert
        TaskResponse savedTask = taskService.getById(taskResponse.getId());
        System.out.println(savedTask);
        assertEquals(taskItemResponse, savedTask.getItems().get(0));
    }
}
