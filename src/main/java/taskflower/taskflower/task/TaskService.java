package taskflower.taskflower.task;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(long id) throws TaskNotFoundExeption {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Task not found by id: " + id));
    }

    public List<Task> findAllByUser(String email) {
        return taskRepository.findAllByEmail(email);
    }

    public Task updateTask(long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

}
