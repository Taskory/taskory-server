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


    public Task save(TaskDto saveTaskRequest, User user) {
        Task task = taskMapper.convertTaskDtoToTask(saveTaskRequest);
        task.setUser(user);

        return taskRepository.save(task);
    }


    public TaskDto getTaskById(long id) throws TaskNotFoundExeption {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));
        return taskMapper.convertTaskToTaskDto(task);
    }

    public List<Task> findAllByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return taskRepository.findAllByUser(user);
    }

    public Task updateTask(long id, TaskDto taskDto) throws TaskNotFoundExeption {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));
        Task updateTask = taskMapper.updateTaskWithSaveTaskRequest(task, taskDto);
        return taskRepository.save(updateTask);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

}
