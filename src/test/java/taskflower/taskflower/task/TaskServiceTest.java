package taskflower.taskflower.task;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.security.model.SignupRequset;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserExistsExeption;

import java.util.List;
import java.util.Random;

@SpringBootTest
class TaskServiceTest {

    private final TaskService taskService;
    private final UserService userService;


    @Autowired
    public TaskServiceTest(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    private User signupRequest;

    @BeforeEach
    void setUser() throws UserExistsExeption {
        signupRequest = createTestUser();
    }

    @AfterEach
    void deleteUser() {
        List<Task> tasks = taskService.findAllByUserEmail(signupRequest.getEmail());
        for (Task task : tasks) {
            taskService.deleteById(task.getId());
        }
        userService.deleteById(signupRequest.getId());
    }

    @Test
//    @WithUserDetails(value = "test@test.test")      // Test userDetail(security test)
    @DisplayName("Task 생성 및 조회")
    void createTask() throws TaskNotFoundExeption {

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTag("test tag");

        Task savedTask = taskService.save(taskDto, signupRequest);

        System.out.println(savedTask.toString());

        Assertions.assertEquals(savedTask, taskService.getTaskById(savedTask.getId()));
    }


    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("Task 수정")
    void updateTask() throws TaskNotFoundExeption {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTag("test tag");

        Task savedTask = taskService.save(taskDto, signupRequest);

        TaskDto updateTaskRequset = new TaskDto();
        updateTaskRequset.setTitle("abcdefg");
        updateTaskRequset.setDescription("............description");
        updateTaskRequset.setStatus(Status.PROGRESS);
        updateTaskRequset.setStartTime(new int[]{2024, 2, 19, 10, 15});
        updateTaskRequset.setEndTime(new int[]{2024, 3, 11, 10, 15});
        updateTaskRequset.setTag("test tag");

        Task updatedTask = taskService.updateTask(savedTask.getId(), updateTaskRequset);

        Assertions.assertEquals(updatedTask, taskService.getTaskById(savedTask.getId()));

    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("task 삭제")
    void deleteTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTag("test tag");

        Task task = taskService.save(taskDto, signupRequest);

        taskService.deleteById(task.getId());

        Assertions.assertThrows(TaskNotFoundExeption.class, () -> {
            taskService.getTaskById(task.getId());
        });
    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("사용자의 모든 task 조회")
    void findTaskByUser() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTag("test tag");

        Task task = taskService.save(taskDto, signupRequest);

        List<Task> savedTasks = taskService.findAllByUserEmail(signupRequest.getEmail());

        Assertions.assertEquals(task, savedTasks.get(savedTasks.size() - 1));
    }

    private User createTestUser() throws UserExistsExeption {

        Random random;
        StringBuilder email;

        do {
            random = new Random();
            email = new StringBuilder();
            email.append((char) (random.nextInt(26) + 'a'));
            String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
            for (int i = 1; i < 10; i++) {
                email.append(characters.charAt(random.nextInt(characters.length())));
            }
            email.append("@example.com");
        } while (userService.existsUser(email.toString()));


        SignupRequset signupRequest = new SignupRequset();
        signupRequest.setName("test");
        signupRequest.setEmail(email.toString());
        signupRequest.setPassword("1234");

        User user = new User(signupRequest);

        return userService.signup(user);
    }
}