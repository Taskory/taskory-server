package codeartist99.taskflower.task.service;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.flow.Flow;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.payload.SaveTaskRequest;
import codeartist99.taskflower.task.payload.TaskResponse;
import codeartist99.taskflower.task.repository.TaskItemRepository;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskItemRepository taskitemRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskItemRepository taskitemRepository) {
        this.taskRepository = taskRepository;
        this.taskitemRepository = taskitemRepository;
    }

    /**
     * Saves a new task for the specified user.
     *
     * @param user the user to associate the task with
     * @param saveTaskRequest the task details to be saved
     * @return a {@link TaskResponse} representing the saved task
     */
    public TaskResponse save(User user, SaveTaskRequest saveTaskRequest) {
        Task task = new Task(user, saveTaskRequest);
        taskRepository.save(task);
        return new TaskResponse(task);
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
     * @return a list of {@link TaskResponse} representing the user's tasks
     */
    public List<TaskResponse> findAll(User user) {
        return taskRepository.findAllByUser(user)
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Finds all tasks filtered by flow or event.
     * At least one of {@code flow} or {@code event} must be non-null.
     *
     * @param user the user whose tasks are to be retrieved
     * @param flow the flow to filter tasks by (nullable)
     * @param event the event to filter tasks by (nullable)
     * @return a list of {@link TaskResponse} representing the filtered tasks
     * @throws IllegalStateException if both {@code flow} and {@code event} are null
     */
    public List<TaskResponse> findAllByFlowOrEvent(User user, Flow flow, Event event) {
        List<Task> tasks;

        if (flow == null && event == null) {
            throw new IllegalStateException("Both flow and event cannot be null.");
        } else if (flow == null) {
            tasks = taskRepository.findAllByUserAndEvent(user, event);
        } else if (event == null) {
            tasks = taskRepository.findAllByUserAndFlow(user, flow);
        } else {
            tasks = taskRepository.findAllByUserAndFlowAndEvent(user, flow, event);
        }

        return tasks.stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing task with new information.
     *
     * @param taskId the ID of the task to be updated
     * @param saveTaskRequest the new task details
     * @return a {@link TaskResponse} representing the updated task
     * @throws TaskNotFoundException if no task with the specified ID is found
     */
    public TaskResponse updateTask(Long taskId, SaveTaskRequest saveTaskRequest) throws TaskNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for id: " + taskId));

        task.update(saveTaskRequest);
        Task updatedTask = taskRepository.save(task);

        return new TaskResponse(updatedTask);
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
