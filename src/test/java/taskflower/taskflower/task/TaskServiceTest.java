package taskflower.taskflower.task;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.security.payload.SignupRequest;
import taskflower.taskflower.task.exception.TaskNotFoundExeption;
import taskflower.taskflower.task.exception.TaskTitleExistException;
import taskflower.taskflower.task.model.Status;
import taskflower.taskflower.task.model.TaskDto;
import taskflower.taskflower.task.tag.*;
import taskflower.taskflower.task.tag.model.Tag;
import taskflower.taskflower.task.tag.exception.TagExistException;
import taskflower.taskflower.task.tag.model.TagDto;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.model.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootTest
class TaskServiceTest {

    private final TaskService taskService;
    private final UserService userService;
    private final TagService tagService;
    private final TagMapper tagMapper;


    @Autowired
    public TaskServiceTest(TaskService taskService, UserService userService, TaskMapper taskMapper, TagService tagService, TagMapper tagMapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    private UserDto testUser;

    @BeforeEach
    void setUser() throws UserAlreadyExistedException {
        testUser = createTestUser();
    }

    @AfterEach
    void deleteUser() {
        List<TaskDto> tasks = taskService.findAllByUserEmail(testUser.getEmail());
        for (TaskDto task : tasks) {
            taskService.deleteById(task.getId());
        }

        List<TagDto> tags = tagService.findAllByUserEmail(testUser.getEmail());
        for (TagDto tagDto : tags) {
            tagService.deleteById(tagDto.getId());
        }
        userService.deleteById(testUser.getId());
    }

    @Test
//    @WithUserDetails(value = "test@test.test")      // Test userDetail(security test)
    @DisplayName("Task 생성 및 조회")
    void createTask() throws TaskNotFoundExeption, TaskTitleExistException {

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        User user = userService.getUserById(testUser.getId());
        TaskDto savedTask = taskService.save(taskDto, user);

        Assertions.assertEquals(savedTask.toString(), taskService.getTaskById(savedTask.getId()).toString());
    }


    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("Task 수정")
    void updateTask() throws TaskNotFoundExeption, TaskTitleExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        User user = userService.getUserById(testUser.getId());
        TaskDto savedTask = taskService.save(taskDto, user);

        TaskDto updateTaskRequest = new TaskDto();
        updateTaskRequest.setTitle("abcdefg");
        updateTaskRequest.setDescription("............description");
        updateTaskRequest.setStatus(Status.PROGRESS);
        updateTaskRequest.setStartTime(new int[]{2024, 2, 19, 10, 15});
        updateTaskRequest.setEndTime(new int[]{2024, 3, 11, 10, 15});
        updateTaskRequest.setTags(getTestTags());

        TaskDto updatedTask = taskService.updateTask(savedTask.getId(), updateTaskRequest);


        Assertions.assertEquals(updatedTask.toString(), taskService.getTaskById(savedTask.getId()).toString());

    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("task 삭제")
    void deleteTask() throws TaskTitleExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        User user = userService.getUserById(testUser.getId());
        TaskDto task = taskService.save(taskDto, user);

        taskService.deleteById(task.getId());

        Assertions.assertThrows(TaskNotFoundExeption.class, () -> {
            taskService.getTaskById(task.getId());
        });
    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("사용자의 모든 task 조회")
    void findTaskByUser() throws TaskTitleExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        User user = userService.getUserById(testUser.getId());
        TaskDto task = taskService.save(taskDto, user);

        List<TaskDto> savedTasks = taskService.findAllByUserEmail(testUser.getEmail());

        Assertions.assertEquals(task.toString(), savedTasks.get(savedTasks.size() - 1).toString());
    }

    private UserDto createTestUser() throws UserAlreadyExistedException {

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


        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("test");
        signupRequest.setEmail(email.toString());
        signupRequest.setPassword("1234");

        return userService.signup(new User(signupRequest));
    }

    private Set<Tag> getTestTags() {
        TagDto tagDto = new TagDto();
        tagDto.setName("test Tag");

        TagDto savedTagDto;
        User user = userService.getUserById(testUser.getId());
        try {
            savedTagDto = tagService.save(tagDto, user);
        } catch (TagExistException exception) {
            List<TagDto> tags = tagService.findAllByUserEmail(testUser.getEmail());
            savedTagDto = tags.get(tags.size() - 1);
        }

        Tag savedTag = tagMapper.convertTagDtoToTag(savedTagDto);
        savedTag.setUser(user);
        Set<Tag> tags = new HashSet<>();
        tags.add(savedTag);

        return tags;

    }
}