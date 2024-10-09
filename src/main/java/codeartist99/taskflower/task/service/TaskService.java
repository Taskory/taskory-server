package codeartist99.taskflower.task.service;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.event.EventNotFoundException;
import codeartist99.taskflower.event.EventRepository;
import codeartist99.taskflower.hashtag.HashtagRepository;
import codeartist99.taskflower.tag.TagNotFoundException;
import codeartist99.taskflower.tag.TagRepository;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.task.exception.InvalidStatusNameException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.model.Status;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.payload.SaveTaskRequest;
import codeartist99.taskflower.task.payload.TaskResponse;
import codeartist99.taskflower.task.payload.TaskSummary;
import codeartist99.taskflower.task.repository.TaskItemRepository;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskItemRepository taskitemRepository;
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final HashtagRepository hashtagRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskItemRepository taskitemRepository, EventRepository eventRepository, TagRepository tagRepository, HashtagRepository hashtagRepository) {
        this.taskRepository = taskRepository;
        this.taskitemRepository = taskitemRepository;
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * Saves a new task for the specified user.
     *
     * @param user the user to associate the task with
     * @param saveTaskRequest the task details to be saved
     * @return a {@link TaskResponse} representing the saved task
     */
    public TaskResponse save(User user, SaveTaskRequest saveTaskRequest)
            throws InvalidStatusNameException, EventNotFoundException, TagNotFoundException {

        Status status = validateAndGetStatus(saveTaskRequest.getStatus());

        Event event = validateAndGetEntityById(
                saveTaskRequest.getEventId(),
                eventRepository,
                () -> new EventNotFoundException("Event not found for ID: " + saveTaskRequest.getEventId())
        );

        Tag tag = validateAndGetEntityById(
                saveTaskRequest.getTagId(),
                tagRepository,
                () -> new TagNotFoundException("Tag not found for ID: " + saveTaskRequest.getTagId())
        );

        Task task = Task.builder()
                .user(user)
                .title(saveTaskRequest.getTitle())
                .event(event)
                .tag(tag)
                .hashtags(saveTaskRequest.getHashtagIds() != null ?
                        hashtagRepository.findAllById(saveTaskRequest.getHashtagIds())
                        :null)
                .description(saveTaskRequest.getDescription())
                .status(status)
                .build();

        taskRepository.save(task);

        return new TaskResponse(task);
    }

    /**
     * Validates and retrieves the corresponding Status enum from the provided status string.
     *
     * @param statusString the string representation of the status
     * @return the corresponding Status enum, or null if statusString is null
     * @throws InvalidStatusNameException if the statusString does not match any valid Status
     */
    private static Status validateAndGetStatus(String statusString) throws InvalidStatusNameException {
        Status status = null;
        if (statusString != null) {
            try {
                status = Status.valueOf(statusString);
            } catch (IllegalArgumentException e) {
                throw new InvalidStatusNameException("Invalid status value: " + statusString +
                        ". Valid values are: " + Arrays.toString(Status.values()));
            }
        }
        return status;
    }

    /**
     * Validates and retrieves an entity by its ID from the given repository.
     *
     * @param id the ID of the entity to retrieve
     * @param repository the repository to search for the entity
     * @param exceptionSupplier the supplier that provides the exception if the entity is not found
     * @param <T> the type of the entity
     * @param <E> the type of exception to be thrown if the entity is not found
     * @return the entity if found, or null if the ID is null
     * @throws E if the entity is not found
     */
    private <T, E extends Exception> T validateAndGetEntityById(Long id, JpaRepository<T, Long> repository, Supplier<E> exceptionSupplier) throws E {
        return id != null ? repository.findById(id).orElseThrow(exceptionSupplier) : null;
    }


    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return a {@link TaskResponse} representing the task
     * @throws TaskNotFoundException if no task with the specified ID is found
     */
    public TaskResponse getById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found for id: " + id));
        return new TaskResponse(task);
    }

    /**
     * Finds all tasks associated with a given user.
     *
     * @param user the user whose tasks are to be retrieved
     * @return a list of {@link TaskSummary} representing the user's tasks
     */
    public List<TaskSummary> findAll(User user) {
        return taskRepository.findAllByUser(user)
                .stream()
                .map(TaskSummary::new)
                .toList();
    }

    /**
     * Finds all tasks filtered by flow or event.
     * At least one of {@code flow} or {@code event} must be non-null.
     *
     * @param user the user whose tasks are to be retrieved
     * @param eventId the eventId to filter tasks by (nullable)
     * @return a list of {@link TaskResponse} representing the filtered tasks
     * @throws IllegalStateException if both {@code flow} and {@code event} are null
     */
    public List<TaskResponse> findAllByEventId(User user, Long eventId) throws EventNotFoundException {
        List<Task> tasks;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found for id: " + eventId));
        if (event == null) {
            throw new IllegalStateException("Both flow and event cannot be null.");
        } else {
            tasks = taskRepository.findAllByUserAndEvent(user, event);
        }

        return tasks.stream()
                .map(TaskResponse::new)
                .toList();
    }

    /**
     * Updates an existing task with new information.
     *
     * @param taskId the ID of the task to be updated
     * @param saveTaskRequest the new task details
     * @return a {@link TaskResponse} representing the updated task
     * @throws TaskNotFoundException if no task with the specified ID is found
     */
    public TaskResponse updateTask(Long taskId, SaveTaskRequest saveTaskRequest) throws TaskNotFoundException, InvalidStatusNameException, EventNotFoundException, TagNotFoundException {
        Status status = validateAndGetStatus(saveTaskRequest.getStatus());

        Event event = validateAndGetEntityById(
                saveTaskRequest.getEventId(),
                eventRepository,
                () -> new EventNotFoundException("Event not found for ID: " + saveTaskRequest.getEventId())
        );

        Tag tag = validateAndGetEntityById(
                saveTaskRequest.getTagId(),
                tagRepository,
                () -> new TagNotFoundException("Tag not found for ID: " + saveTaskRequest.getTagId())
        );

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found for ID: " + taskId));
        Task updateTask = Task.builder()
                .title(saveTaskRequest.getTitle())
                .event(event)
                .tag(tag)
                .hashtags(hashtagRepository.findAllById(saveTaskRequest.getHashtagIds()))
                .description(saveTaskRequest.getDescription())
                .status(status)
                .build();

        task.update(updateTask);

        taskRepository.save(task);
        return new TaskResponse(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted
     * @throws TaskNotFoundException if no task with the specified ID is found
     */
    public void deleteById(Long id) throws TaskNotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found for id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Deletes all tasks and associated task items for the given user.
     *
     * @param user the user whose tasks and associated task items are to be deleted
     */
    public void deleteAllByUser(User user) {
        List<Task> tasks = taskRepository.findAllByUser(user);

        // Batch deletion of task items and tasks
        taskitemRepository.deleteAllByTaskIn(tasks);
        taskRepository.deleteAllByUser(user);
    }
}
