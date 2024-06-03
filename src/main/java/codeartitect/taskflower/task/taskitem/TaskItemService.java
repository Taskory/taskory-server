package codeartitect.taskflower.task.taskitem;

import codeartitect.taskflower.task.Task;
import codeartitect.taskflower.task.TaskNotFoundException;
import codeartitect.taskflower.task.TaskRepository;
import codeartitect.taskflower.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public TaskItemResponse save(User user, SaveTaskItemRequest saveTaskItemRequest) {
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
    public TaskItemResponse getById(Long itemId) {
        TaskItem taskItem = taskItemRepository.findById(itemId).orElseThrow(TaskItemNotFoundException::new);
        return new TaskItemResponse(taskItem);
    }

    /**
     * Find all task items by task info
     * @param taskId Task id
     * @return TaskItemResponse list
     */
    public List<TaskItemResponse> findAllByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        List<TaskItem> items = taskItemRepository.findAllByTask(task);

        List<TaskItemResponse> taskItemList = new ArrayList<>();
        for (TaskItem item : items) {
            taskItemList.add(new TaskItemResponse(item));
        }
        return taskItemList;
    }

    /**
     * Update task item title
     * @param id Task item id
     * @param updateTitle Title for update title of task item
     * @return TaskItemResponse
     */
    public TaskItemResponse updateTitle(Long id, String updateTitle) {
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
    public TaskItemResponse setCompleted(Long id, boolean completed) {
        TaskItem taskItem = taskItemRepository.findById(id).orElseThrow(TaskItemNotFoundException::new);
        taskItem.setCompleted(completed);
        TaskItem updatedTaskItem = taskItemRepository.save(taskItem);
        return new TaskItemResponse(updatedTaskItem);
    }

    /**
     * Delete task item by task item id
     * @param id Task item id for delete
     */
    public void deleteById(Long id) {
        if (taskItemRepository.existsById(id)) {
            taskItemRepository.deleteById(id);
        } else throw new TaskItemNotFoundException();
    }

}
