package codeartist99.taskflower.task.service;

import codeartist99.taskflower.task.exception.TaskItemNotFoundException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.model.TaskItem;
import codeartist99.taskflower.task.payload.SaveTaskItemRequest;
import codeartist99.taskflower.task.payload.TaskItemResponse;
import codeartist99.taskflower.task.repository.TaskItemRepository;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskItemService {

    private final TaskItemRepository taskItemRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskItemService(TaskItemRepository taskItemRepository, TaskRepository taskRepository) {
        this.taskItemRepository = taskItemRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Save task item
     * @param user User information
     * @param saveTaskItemRequest Information to save task item
     * @return TaskItemResponse
     */
    public TaskItemResponse save(User user, SaveTaskItemRequest saveTaskItemRequest) throws TaskNotFoundException {
        Task task = taskRepository.findById(saveTaskItemRequest.getTaskId()).orElseThrow(TaskNotFoundException::new);
        TaskItem taskItem = new TaskItem(user, task, saveTaskItemRequest);

        taskItemRepository.save(taskItem);

        return new TaskItemResponse(taskItem);
    }

    /**
     * Get task item by task item id
     * @param itemId Task item id
     * @return TaskItemResponse
     */
    public TaskItemResponse getById(Long itemId) throws TaskItemNotFoundException {
        TaskItem taskItem = taskItemRepository.findById(itemId).orElseThrow(TaskItemNotFoundException::new);
        return new TaskItemResponse(taskItem);
    }

    /**
     * Find all task items by task info
     * @param taskId Task id
     * @return TaskItemResponse list
     */
    public List<TaskItemResponse> findAllByTaskId(Long taskId) throws TaskNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        List<Optional<TaskItem>> items = taskItemRepository.findAllByTask(task);

        List<TaskItemResponse> taskItemList = new ArrayList<>();
        for (Optional<TaskItem> item : items) {
            item.ifPresent(value -> taskItemList.add(new TaskItemResponse(value)));
        }
        return taskItemList;
    }

    /**
     * Update task item title
     * @param id Task item id
     * @param updateTitle Title for update title of task item
     * @return TaskItemResponse
     */
    public TaskItemResponse updateTitle(Long id, String updateTitle) throws TaskItemNotFoundException {
        TaskItem taskItem = taskItemRepository.findById(id).orElseThrow(TaskItemNotFoundException::new);
        taskItem.setTitle(updateTitle);
        TaskItem updatedTaskItem = taskItemRepository.save(taskItem);
        return new TaskItemResponse(updatedTaskItem);
    }

    /**
     * Update 'completed' of task item
     * @param id Task item id
     * @param completed boolean
     * @return TaskItemResponse
     */
    public TaskItemResponse setCompleted(Long id, boolean completed) throws TaskItemNotFoundException {
        TaskItem taskItem = taskItemRepository.findById(id).orElseThrow(TaskItemNotFoundException::new);
        taskItem.setCompleted(completed);
        TaskItem updatedTaskItem = taskItemRepository.save(taskItem);
        return new TaskItemResponse(updatedTaskItem);
    }

    /**
     * Delete task item by task item id
     * @param id Task item id for delete
     */
    public void deleteById(Long id) throws TaskItemNotFoundException {
        if (taskItemRepository.existsById(id)) {
            taskItemRepository.deleteById(id);
        } else throw new TaskItemNotFoundException();
    }

}
