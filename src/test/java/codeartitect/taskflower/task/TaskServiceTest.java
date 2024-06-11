package codeartitect.taskflower.task;

import codeartitect.taskflower.tag.model.Tag;
import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.event.EventRepository;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.flow.FlowRepository;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.task.exception.TaskNotFoundException;
import codeartitect.taskflower.task.model.Status;
import codeartitect.taskflower.task.model.Task;
import codeartitect.taskflower.task.payload.SaveTaskRequest;
import codeartitect.taskflower.task.payload.TaskResponse;
import codeartitect.taskflower.task.repository.TaskRepository;
import codeartitect.taskflower.task.service.TaskService;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.model.User;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User user;
    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, InvalidZoneIdException {
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
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);
        user = userRepository.findById(userResponse.getId()).orElseThrow();
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving task
     * Flow, Event, Tag, Hashtag null
     */
    @Test
    @DisplayName("save task and get task test")
    void save() throws TaskNotFoundException {
//        Arrange
        String title = "test title";
        Flow flow = null;
        Event event = null;
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, flow, event, tag, hashtags, description, Status.TODO);

//        Act
        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);

//        Assert
        assertEquals(taskResponse.toString(), taskService.getById(taskResponse.getId()).toString());
    }

    /**
     * Test for find all tasks by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() {
//        Arrange
//        first task
        String title = "test title";
        Flow flow = null;
        Event event = null;
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, flow, event, tag, hashtags, description, Status.TODO);

//        second task
        String title2 = "test title2";
        Flow flow2 = null;
        Event event2 = null;
        Tag tag2 = null;
        List<Hashtag> hashTags2 = null;
        String description2 = "test description2";
        SaveTaskRequest saveTaskRequest2 = new SaveTaskRequest(title2, flow2, event2, tag2, hashTags2, description2, Status.IN_PROGRESS);

//        save a first task
        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);
//        save a second task
        TaskResponse taskResponse2 = taskService.save(user, saveTaskRequest2);

//        Act
//        find all tasks
        List<TaskResponse> taskResponseList = taskService.findAll(user);

//        Assert
//        find a first task
        TaskResponse actualTaskResponse = taskResponseList.get(0);
//        find a second task
        TaskResponse actualTaskResponse2 = taskResponseList.get(1);

        assertEquals(taskResponse.toString(), actualTaskResponse.toString());
        assertEquals(taskResponse2.toString(), actualTaskResponse2.toString());
    }

    /**
     * Test for get all tasks filtered by flow and event
     */
    @Test
    @DisplayName("find task filtered by flow or event")
    void findAllByFlowOrEvent() {
        Flow flow = new Flow(null, user, "flow title", null, "flow description");
        flowRepository.save(flow);
        Event event = new Event(null, user, "event title", null, null, "event description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null);
        eventRepository.save(event);
        Task task = new Task(null, user, "task title", flow, event, null, null, "task description", Status.TODO, null);
        taskRepository.save(task);

        List<TaskResponse> taskResponseList = taskService.findAllByFlowOrEvent(user, flow, event);
        TaskResponse savedTask = taskResponseList.get(0);

        assertEquals(flow.getId(), savedTask.getFlow().getId());
        assertEquals(event.getId(), savedTask.getEvent().getId());
        assertEquals(task.getId(), savedTask.getId());
    }

    /**
     * Test for update a task
     */
    @Test
    @DisplayName("update a task")
    void updateTask() throws TaskNotFoundException {
//        Arrange
//        save a task
        String title = "test title";
        Flow flow = null;
        Event event = null;
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, flow, event, tag, hashtags, description, Status.TODO);

        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);

//        update a task
        String updateTitle = "test title2";
        Flow updateFlow = null;
        Event updateEvent = null;
        Tag updateTag = null;
        List<Hashtag> updateHashtags = null;
        String updateDescription = "test description2";
        SaveTaskRequest updateTaskRequest = new SaveTaskRequest(updateTitle, updateFlow, updateEvent, updateTag, updateHashtags, updateDescription, Status.IN_PROGRESS);

//        Act
        TaskResponse updateTaskResponse = taskService.updateTask(taskResponse.getId(), updateTaskRequest);

//        Assert
        assertEquals(updateTaskResponse.toString(), taskService.getById(taskResponse.getId()).toString());
    }

    /**
     * Test for delete task
     */
    @Test
    @DisplayName("delete task")
    void deleteById() throws TaskNotFoundException {
//        Arrange
//        save a task
        String title = "test title";
        Flow flow = null;
        Event event = null;
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, flow, event, tag, hashtags, description, Status.TODO);

        TaskResponse taskResponse = taskService.save(user, saveTaskRequest);
        Long taskId = taskResponse.getId();

//        Act
        taskService.deleteById(taskResponse.getId());

//        Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getById(taskId));
    }
}