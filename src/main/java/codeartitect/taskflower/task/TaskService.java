package codeartitect.taskflower.task;

import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Save task
     * @param user User information
     * @param saveTaskRequest Information to save a task
     * @return TaskResponse
     */
    public TaskResponse save(User user, SaveTaskRequest saveTaskRequest) {
        Task task = new Task(user, saveTaskRequest);
        taskRepository.save(task);

        return new TaskResponse(task);
    }

    /**
     * Get task by task id
     * @param id Id of task
     * @return TaskResponse
     */
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
        return new TaskResponse(task);
    }

    /**
     * Find all tasks by user info
     * @param user User information
     * @return TaskResponse list
     */
    public List<TaskResponse> findAll(User user) {
        List<Task> tasks = taskRepository.findAllByUser(user);

        List<TaskResponse> taskResponseList = new ArrayList<>();
        for (Task task : tasks) {
            taskResponseList.add(new TaskResponse(task));
        }

        return taskResponseList;
    }

    /**
     * Find all task filtered by flow info or event info
     * Both flow and event should not be null
     * Either flow or event should be null
     * @param user User information
     * @param flow Flow information - nullable
     * @param event Event information - nullable
     * @return TaskResponse list
     */
    public List<TaskResponse> findAllByFlowOrEvent(User user, Flow flow, Event event) {
        List<Task> tasks;
        if (flow == null && event == null) {
            throw new IllegalStateException("Invalid parameter - flow, event");
        } else if (flow == null) {
            tasks = taskRepository.findAllByUserAndEvent(user, event);
        } else if (event == null) {
            tasks = taskRepository.findAllByUserAndFlow(user, flow);
        } else {
            tasks = taskRepository.findAllByUserAndFlowAndEvent(user, flow, event);
        }

        List<TaskResponse> taskResponseList = new ArrayList<>();
        for (Task task : tasks) {
            taskResponseList.add(new TaskResponse(task));
        }

        return taskResponseList;
    }

    /**
     * Update task information
     * @param taskId Task id for update task information
     * @param saveTaskRequest Task information to be updated
     * @return TaskResponse
     */
    public TaskResponse updateTask(Long taskId, SaveTaskRequest saveTaskRequest) {
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        task.update(saveTaskRequest);

        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }

    /**
     * Delete task
     * @param id Task id for delete task
     */
    public void deleteById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else throw new TaskNotFoundException();
    }
}
