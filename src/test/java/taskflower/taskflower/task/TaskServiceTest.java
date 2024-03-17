package taskflower.taskflower.task;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.security.model.SignupRequset;
import taskflower.taskflower.task.tag.*;
import taskflower.taskflower.task.tag.Tag;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserExistsExeption;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootTest
class TaskServiceTest {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final TagService tagService;
    private final TagMapper tagMapper;


    @Autowired
    public TaskServiceTest(TaskService taskService, UserService userService, TaskMapper taskMapper, TagService tagService, TagMapper tagMapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    private User testUser;

    @BeforeEach
    void setUser() throws UserExistsExeption {
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
    void createTask() throws TaskNotFoundExeption, TagExistException {

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        TaskDto savedTask = taskService.save(taskDto, testUser);

        Assertions.assertEquals(savedTask, taskService.getTaskById(savedTask.getId()));
    }


    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("Task 수정")
    void updateTask() throws TaskNotFoundExeption, TagExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        TaskDto savedTask = taskService.save(taskDto, testUser);

        TaskDto updateTaskRequset = new TaskDto();
        updateTaskRequset.setTitle("abcdefg");
        updateTaskRequset.setDescription("............description");
        updateTaskRequset.setStatus(Status.PROGRESS);
        updateTaskRequset.setStartTime(new int[]{2024, 2, 19, 10, 15});
        updateTaskRequset.setEndTime(new int[]{2024, 3, 11, 10, 15});
        updateTaskRequset.setTags(getTestTags());

        TaskDto updatedTask = taskService.updateTask(savedTask.getId(), updateTaskRequset);


        Assertions.assertEquals(updatedTask, taskService.getTaskById(savedTask.getId()));

    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("task 삭제")
    void deleteTask() throws TagExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        TaskDto task = taskService.save(taskDto, testUser);

        taskService.deleteById(task.getId());

        Assertions.assertThrows(TaskNotFoundExeption.class, () -> {
            taskService.getTaskById(task.getId());
        });
    }

    @Test
//    @WithUserDetails(value = "test@test.test")
    @DisplayName("사용자의 모든 task 조회")
    void findTaskByUser() throws TagExistException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("test title");
        taskDto.setDescription("test description.....");
        taskDto.setStatus(Status.TODO);
        taskDto.setStartTime(new int[]{2024, 2, 16, 10, 15});
        taskDto.setEndTime(new int[]{2024, 3, 16, 10, 15});
        taskDto.setTags(getTestTags());

        TaskDto task = taskService.save(taskDto, testUser);

        List<TaskDto> savedTasks = taskService.findAllByUserEmail(testUser.getEmail());

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

    private Set<Tag> getTestTags() {
        TagDto tagDto = new TagDto();
        tagDto.setName("test Tag");

        TagDto savedTagDto;
        try {
            savedTagDto = tagService.save(tagDto, testUser);
        } catch (TagExistException exception) {
            List<TagDto> tags = tagService.findAllByUserEmail(testUser.getEmail());
            savedTagDto = tags.get(tags.size() - 1);
        }

        Tag savedTag = tagMapper.convertTagDtoToTag(savedTagDto);
        savedTag.setUser(testUser);
        Set<Tag> tags = new HashSet<>();
        tags.add(savedTag);

        return tags;

    }
}