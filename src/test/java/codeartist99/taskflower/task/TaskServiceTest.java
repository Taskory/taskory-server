package codeartist99.taskflower.task;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.event.EventNotFoundException;
import codeartist99.taskflower.event.EventRepository;
import codeartist99.taskflower.setup.ArrangeTest;
import codeartist99.taskflower.tag.TagNotFoundException;
import codeartist99.taskflower.task.exception.InvalidStatusNameException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.payload.SaveTaskRequest;
import codeartist99.taskflower.task.payload.TaskResponse;
import codeartist99.taskflower.task.payload.TaskSummary;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "TO_DO");

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
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "TO_DO");

        // Second task
        String title2 = "test title2";
        List<Long> hashTags2 = Collections.emptyList();
        String description2 = "test description2";
        SaveTaskRequest saveTaskRequest2 = new SaveTaskRequest(title2, null, tempTag.getId(), hashTags2, description2, "IN_PROGRESS");

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
        Event event = new Event(null, tempUser, "event title", tempTag, null, "event description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null);
        eventRepository.save(event);
        Task task = new Task(null, tempUser, "task title", event, tempTag, null, "task description", Status.TO_DO, null);
        taskRepository.save(task);

        List<TaskResponse> taskResponseList = taskService.findAllByEventId(tempUser, event.getId());
        TaskResponse savedTask = taskResponseList.get(0);

        assertEquals(event.getId(), savedTask.getEvent().getId());
        assertEquals(task.getId(), savedTask.getId());
    }

    /**
     * Test for update a task
     */
    @Test
    @DisplayName("update a task")
    void updateTask() throws TaskNotFoundException, InvalidStatusNameException, TagNotFoundException, EventNotFoundException {
//        Arrange
//        save a task
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "TO_DO");

        TaskResponse taskResponse = taskService.save(tempUser, saveTaskRequest);

//        update a task
        String updateTitle = "test title2";
        List<Long> updateHashtags = Collections.emptyList();
        String updateDescription = "test description2";
        SaveTaskRequest updateTaskRequest = new SaveTaskRequest(updateTitle, null, tempTag.getId(), updateHashtags, updateDescription, "IN_PROGRESS");

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
        SaveTaskRequest saveTaskRequest = new SaveTaskRequest(title, null, tempTag.getId(), hashtags, description, "TO_DO");

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
        SaveTaskRequest saveTaskRequest1 = new SaveTaskRequest(title1, null, tempTag.getId(), Collections.emptyList(), description1, "TO_DO");

        TaskResponse taskResponse1 = taskService.save(tempUser, saveTaskRequest1);

        // Create second task with the same tag
        String title2 = "Task 2";
        String description2 = "Description 2";
        SaveTaskRequest saveTaskRequest2 = new SaveTaskRequest(title2, null, tempTag.getId(), Collections.emptyList(), description2, "IN_PROGRESS");

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