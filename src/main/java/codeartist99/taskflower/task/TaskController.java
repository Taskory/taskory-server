package codeartist99.taskflower.task;

import codeartist99.taskflower.event.EventNotFoundException;
import codeartist99.taskflower.event.EventService;
import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.tag.TagNotFoundException;
import codeartist99.taskflower.task.exception.InvalidStatusNameException;
import codeartist99.taskflower.task.exception.TaskItemNotFoundException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.payload.*;
import codeartist99.taskflower.task.service.TaskItemService;
import codeartist99.taskflower.task.service.TaskService;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing tasks and task items.
 * Provides endpoints for creating, retrieving, updating, and deleting tasks and task items.
 */
@RestController
@RequestMapping("${app.url-base}/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskItemService taskItemService;
    private final UserRepository userRepository;
    private final EventService eventService;

    @Autowired
    public TaskController(TaskService taskService, TaskItemService taskItemService, UserRepository userRepository, EventService eventService) {
        this.taskService = taskService;
        this.taskItemService = taskItemService;
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    /**
     * Creates a new task for the authenticated user.
     *
     * @param userPrincipal the authenticated user details
     * @param saveTaskRequest the request payload containing task details
     * @return the response containing the saved task details
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@CurrentUser UserPrincipal userPrincipal, @RequestBody SaveTaskRequest saveTaskRequest) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        TaskResponse response = null;
        try {
            response = taskService.save(user, saveTaskRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidStatusNameException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        } catch (EventNotFoundException | TagNotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return the response containing the task details
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse response;
        try {
            response = taskService.getById(id);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all tasks for the authenticated user.
     *
     * @param userPrincipal the authenticated user details
     * @return the response containing a list of tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskSummary>> getAllTasks(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<TaskSummary> responses = taskService.findAll(user);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves tasks for the authenticated user, filtered by event ID if provided.
     *
     * @param userPrincipal the authenticated user details
     * @param eventId the ID of the event to filter tasks by (optional)
     * @return the response containing a list of filtered tasks
     */
    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponse>> getTasksByEvent(@CurrentUser UserPrincipal userPrincipal, @RequestParam(required = false) Long eventId) {
        try {
            EventResponse eventResponse = eventService.getById(eventId);
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<TaskResponse> responses = taskService.findAllByEventId(user, eventResponse.getId());
            return ResponseEntity.ok(responses);
        } catch (EventNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an existing task with new details.
     *
     * @param id the ID of the task to update
     * @param saveTaskRequest the request payload containing new task details
     * @return the response containing the updated task details
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody SaveTaskRequest saveTaskRequest) {
        TaskResponse response;
        try {
            response = taskService.updateTask(id, saveTaskRequest);
        } catch (TagNotFoundException | EventNotFoundException | TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidStatusNameException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return a response indicating whether the deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        try {
            taskService.deleteById(id);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a new task item to an existing task.
     *
     * @param userPrincipal the authenticated user details
     * @param saveTaskItemRequest the request payload containing task item details
     * @return the response containing the saved task item details
     */
    @PostMapping("/items")
    public ResponseEntity<TaskItemResponse> createTaskItem(@CurrentUser UserPrincipal userPrincipal, @RequestBody SaveTaskItemRequest saveTaskItemRequest) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        TaskItemResponse response;
        try {
            response = taskItemService.save(user, saveTaskItemRequest);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a task item by its ID.
     *
     * @param id the ID of the task item to retrieve
     * @return the response containing the task item details
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<TaskItemResponse> getTaskItemById(@PathVariable Long id) {
        TaskItemResponse response;
        try {
            response = taskItemService.getById(id);
        } catch (TaskItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the title of an existing task item.
     *
     * @param id the ID of the task item to update
     * @param newTitle the new title for the task item
     * @return the response containing the updated task item details
     */
    @PatchMapping("/items/{id}/title")
    public ResponseEntity<TaskItemResponse> updateTaskItemTitle(@PathVariable Long id, @RequestParam String newTitle) {
        TaskItemResponse response;
        try {
            response = taskItemService.updateTitle(id, newTitle);
        } catch (TaskItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the completion status of an existing task item.
     *
     * @param id the ID of the task item to update
     * @param completed the new completion status (true if completed, false otherwise)
     * @return the response containing the updated task item details
     */
    @PatchMapping("/items/{id}/completed")
    public ResponseEntity<TaskItemResponse> setTaskItemCompleted(@PathVariable Long id, @RequestParam boolean completed) {
        TaskItemResponse response;
        try {
            response = taskItemService.setCompleted(id, completed);
        } catch (TaskItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a task item by its ID.
     *
     * @param id the ID of the task item to delete
     * @return a response indicating whether the deletion was successful
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteTaskItemById(@PathVariable Long id) {
        try {
            taskItemService.deleteById(id);
        } catch (TaskItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
