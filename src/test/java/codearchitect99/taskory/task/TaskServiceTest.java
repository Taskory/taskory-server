package codearchitect99.taskory.task;

import codearchitect99.taskory.event.Event;
import codearchitect99.taskory.event.EventNotFoundException;
import codearchitect99.taskory.event.EventRepository;
import codearchitect99.taskory.setup.ArrangeTest;
import codearchitect99.taskory.tag.TagNotFoundException;
import codearchitect99.taskory.task.exception.InvalidDeadlineException;
import codearchitect99.taskory.task.exception.InvalidStatusNameException;
import codearchitect99.taskory.task.exception.TaskNotFoundException;
import codearchitect99.taskory.task.model.Status;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.task.payload.SaveTaskRequest;
import codearchitect99.taskory.task.payload.TaskItemDto;
import codearchitect99.taskory.task.payload.TaskResponse;
import codearchitect99.taskory.task.payload.TaskSummary;
import codearchitect99.taskory.task.repository.TaskRepository;
import codearchitect99.taskory.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest extends ArrangeTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Test for saving task
     * Flow, Event, Tag, Hashtag null
     */
    @Test
    @DisplayName("save task and get task test")
    void save() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
//        Arrange
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        List<TaskItemDto> items = new ArrayList<>();
        TaskItemDto taskItemDto = new TaskItemDto(null, null, "test item title", false);
        items.add(taskItemDto);
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "BACKLOG", items, null);

//        Act
        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);

//        Assert
        assertEquals(taskResponse.toString(), taskService.getById(taskResponse.getId()).toString());
    }

    /**
     * Test for find all tasks by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() throws InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
        // Arrange
        // First task
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "BACKLOG", null, null);

        // Second task
        String title2 = "test title2";
        List<Long> hashTags2 = Collections.emptyList();
        String description2 = "test description2";
        SaveTaskRequest saveTaskRequest2 = new SaveTaskRequest(title2, null, tempTag.getId(), hashTags2, description2, "BACKLOG", null, null);

        // Save tasks
        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);
        TaskResponse taskResponse2 = taskService.save(tempUser, saveTaskRequest2);

        // Act
        // Find all tasks
        List<TaskSummary> taskResponseList = taskService.findAll(tempUser);

        // Assert
        // Verify each task is in the list
        boolean firstTaskFound = taskResponseList.stream()
                .anyMatch(task -> task.getId().equals(taskResponse.getId()));

        boolean secondTaskFound = taskResponseList.stream()
                .anyMatch(task -> task.getId().equals(taskResponse2.getId()));

        assertTrue(firstTaskFound);
        assertTrue(secondTaskFound);
    }


    /**
     * Test for get all tasks filtered by flow and event
     */
    @Test
    @DisplayName("find task filtered by flow or event")
    void findAllByEventId() throws EventNotFoundException {
        Event event = Event.builder()
                .id(null)
                .user(tempUser)
                .title("event title")
                .tag(tempTag)
                .tasks(null)
                .startDateTime(LocalDateTime.now())
                .dueDateTime(LocalDateTime.now().plusDays(1))
                .description("event description")
                .location(null)
                .hashtags(null)
                .build();
        eventRepository.save(event);

        Task task = Task.builder()
                .id(null)
                .user(tempUser)
                .title("task title")
                .event(event)
                .tag(tempTag)
                .hashtags(null)
                .description("task description")
                .status(Status.BACKLOG)
                .items(null)
                .build();

        taskRepository.save(task);

        List<TaskSummary> taskResponseList = taskService.findAllByEventId(tempUser, event.getId());
        TaskSummary savedTask = taskResponseList.get(0);

        assertEquals(event.getId(), savedTask.getEvent().getId());
        assertEquals(task.getId(), savedTask.getId());
    }

    /**
     * Test for update a task
     */
    @Test
    @DisplayName("update a task")
    void updateTask() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException, InvalidDeadlineException {
//        Arrange
//        save a task
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        TaskItemDto taskItemDto = new TaskItemDto(null, null, "saved item 1", false);
        TaskItemDto taskItemDto2 = new TaskItemDto(null, null, "saved item 2", false);
        TaskItemDto taskItemDto3 = new TaskItemDto(null, null, "saved item 3", true);
        List<TaskItemDto> items = new ArrayList<>();
        items.add(taskItemDto);
        items.add(taskItemDto2);
        items.add(taskItemDto3);
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "BACKLOG", items, null);

        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);

//        update a task
        String updateTitle = "test title2";
        List<Long> updateHashtags = Collections.emptyList();
        String updateDescription = "test description2";
        taskItemDto2.setTitle("saved item 2 to update");

        List<TaskItemDto> updateItems = new ArrayList<>();
        items.add(taskItemDto);
        items.add(taskItemDto2);
        SaveTaskRequest updateTaskRequest = new SaveTaskRequest(updateTitle, null, tempTag.getId(), updateHashtags, updateDescription, "BACKLOG", updateItems, null);

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
    void deleteById() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
//        Arrange
//        save a task
        String title = "test title";
        List<Long> hashtags = null;
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "BACKLOG", null, null);

        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);
        Long taskId = taskResponse.getId();

//        Act
        taskService.deleteById(taskResponse.getId());

//        Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getById(taskId));
    }

    @Test
    @DisplayName("Find all tasks by tag IDs")
    void findAllByTags() throws InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
        // Arrange
        // Create first task with a specific tag
        String title1 = "Task 1";
        String description1 = "Description 1";
        SaveTaskRequest saveTaskRequest1 = new SaveTaskRequest(title1, null, tempTag.getId(), Collections.emptyList(), description1, "BACKLOG", null, null);

        TaskResponse taskResponse1 = taskService.save(tempUser, saveTaskRequest1);

        // Create second task with the same tag
        String title2 = "Task 2";
        String description2 = "Description 2";
        SaveTaskRequest saveTaskRequest2 = new SaveTaskRequest(title2, null, tempTag.getId(), Collections.emptyList(), description2, "BACKLOG", null, null);

        TaskResponse taskResponse2 = taskService.save(tempUser, saveTaskRequest2);

        // Act
        // Call findAllByTags with the tag ID of the created tasks
        List<TaskSummary> taskSummaries = taskService.findAllByTags(List.of(tempTag.getId()));

        // Assert
        boolean task1Found = taskSummaries.stream()
                .anyMatch(task -> task.getId().equals(taskResponse1.getId()) && task.getTitle().equals(title1));
        boolean task2Found = taskSummaries.stream()
                .anyMatch(task -> task.getId().equals(taskResponse2.getId()) && task.getTitle().equals(title2));

        assertTrue(task1Found, "Task 1 should be retrieved");
        assertTrue(task2Found, "Task 2 should be retrieved");
    }
}