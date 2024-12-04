package codeartist99.taskflower.task.service;

import codeartist99.taskflower.task.exception.TaskItemNotFoundException;
import codeartist99.taskflower.task.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class TaskItemService {

//    private final TaskItemRepository taskItemRepository;
//    private final TaskRepository taskRepository;

//    @Autowired
//    public TaskItemService(TaskItemRepository taskItemRepository, TaskRepository taskRepository) {
//        this.taskItemRepository = taskItemRepository;
//        this.taskRepository = taskRepository;
//    }

    /**
     * @deprecated
     * Saves a new task item for the specified user and task.
     *
     * @param user the user who owns the task item
     * @param taskItemDto the request containing task item details
     * @return the saved task item response
     * @throws TaskNotFoundException if the task with the specified ID is not found
     */
//    public TaskItemResponse save(User user, TaskItemDto taskItemDto) throws TaskNotFoundException {
//        Task task = taskRepository.findById(taskItemDto.getTaskId()).orElseThrow(TaskNotFoundException::new);
//        TaskItem taskItem = new TaskItem(user, task, taskItemDto);
//
//        taskItemRepository.save(taskItem);
//
//        return new TaskItemResponse(taskItem);
//    }

    /**
     * @deprecated
     * Retrieves a task item by its ID.
     *
     * @param itemId the ID of the task item to retrieve
     * @return the task item response
     * @throws TaskItemNotFoundException if no task item is found with the given ID
     */
//    public TaskItemResponse getById(Long itemId) throws TaskItemNotFoundException {
//        TaskItem taskItem = taskItemRepository.findById(itemId).orElseThrow(TaskItemNotFoundException::new);
//        return new TaskItemResponse(taskItem);
//    }

    /**
     * @deprecated
     * Retrieves all task items associated with a given task ID.
     *
     * @param taskId the ID of the task
     * @return a list of task item responses
     * @throws TaskNotFoundException if no task is found with the given ID
     */
//    public List<TaskItemResponse> findAllByTaskId(Long taskId) throws TaskNotFoundException {
//        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
//        List<TaskItem> items = taskItemRepository.findAllByTask(task);
//
//        return items.stream()
//                .map(TaskItemResponse::new)
//                .toList();
//    }

    /**
     * @deprecated
     * Updates the title of an existing task item.
     *
     * @param id the ID of the task item to update
     * @param updateTitle the new title for the task item
     * @return the updated task item response
     * @throws TaskItemNotFoundException if no task item is found with the given ID
     */
//    public TaskItemResponse updateTitle(Long id, String updateTitle) throws TaskItemNotFoundException {
//        TaskItem taskItem = taskItemRepository.findById(id).orElseThrow(TaskItemNotFoundException::new);
//        taskItem.setTitle(updateTitle);
//        return new TaskItemResponse(taskItemRepository.save(taskItem));
//    }

    /**
     * @deprecated
     * Updates the completion status of an existing task item.
     *
     * @param id the ID of the task item to update
     * @param completed the new completion status (true if completed, false if not)
     * @return the updated task item response
     * @throws TaskItemNotFoundException if no task item is found with the given ID
     */
//    public TaskItemResponse setCompleted(Long id, boolean completed) throws TaskItemNotFoundException {
//        TaskItem taskItem = taskItemRepository.findById(id).orElseThrow(TaskItemNotFoundException::new);
//        taskItem.setCompleted(completed);
//        return new TaskItemResponse(taskItemRepository.save(taskItem));
//    }

    /**
     * @deprecated
     * Deletes a task item by its ID.
     *
     * @param id the ID of the task item to delete
     * @throws TaskItemNotFoundException if no task item is found with the given ID
     */
//    public void deleteById(Long id) throws TaskItemNotFoundException {
//        if (taskItemRepository.existsById(id)) {
//            taskItemRepository.deleteById(id);
//        } else {
//            throw new TaskItemNotFoundException();
//        }
//    }
}
