package taskflower.taskflower.task;

import org.springframework.stereotype.Service;
import taskflower.taskflower.task.exception.TaskNotFoundExeption;
import taskflower.taskflower.task.exception.TaskTitleExistException;
import taskflower.taskflower.task.model.Task;
import taskflower.taskflower.task.model.TaskDto;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserRepository;
import taskflower.taskflower.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }


    public TaskDto save(TaskDto saveTaskRequest, User user) throws TaskTitleExistException {
        if (taskRepository.existsByTitleAndUser(saveTaskRequest.getTitle(), user)) throw new TaskTitleExistException();

        Task task = taskMapper.convertTaskDtoToTask(saveTaskRequest);
        task.setUser(user);
        Task savedTask = taskRepository.save(task);

        return taskMapper.convertTaskToTaskDto(savedTask);
    }


    public TaskDto getTaskById(long id) throws TaskNotFoundExeption {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));
        return taskMapper.convertTaskToTaskDto(task);
    }

    public List<TaskDto> findAllByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        List<Task> tasks = taskRepository.findAllByUser(user);

        List<TaskDto> result = new ArrayList<>();

        for (Task task : tasks) {
            result.add(taskMapper.convertTaskToTaskDto(task));
        }

        return result;
    }

    public TaskDto updateTask(long id, TaskDto taskDto) throws TaskNotFoundExeption {
        Task preTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));

        Task task = taskMapper.updateTaskWithTaskDto(preTask, taskDto);

        Task uppdatedTask = taskRepository.save(task);

        return taskMapper.convertTaskToTaskDto(uppdatedTask);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

}
