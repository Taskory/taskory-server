package taskflower.taskflower.task;

import org.springframework.stereotype.Service;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserRepository;
import taskflower.taskflower.user.UserService;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }


    public Task save(SaveTaskRequset saveTaskRequest) {
        Task task = taskMapper.convertSaveTaskRequestToTask(saveTaskRequest);
        task.setUser(user);

        return taskRepository.save(task);
    }


    public Task getTaskById(long id) throws TaskNotFoundExeption {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));
    }

    public List<Task> findAllByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return taskRepository.findAllByUser(user);
    }

    public Task updateTask(long id, SaveTaskRequset saveTaskRequset) throws TaskNotFoundExeption {
        Task task = this.getTaskById(id);
        Task updateTask = taskMapper.updateTaskWithSaveTaskRequest(task, saveTaskRequset);
        return taskRepository.save(updateTask);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

}
