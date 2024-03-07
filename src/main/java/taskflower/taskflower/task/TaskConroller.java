package taskflower.taskflower.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.security.CurrentUser;
import taskflower.taskflower.security.UserDetailsImpl;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskConroller {

    private UserService userService;
    private TaskService taskService;

    @Autowired
    public TaskConroller(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> save(@CurrentUser UserDetailsImpl userDetails, @RequestBody SaveTaskRequset saveTaskRequset) {
        User user = userService.getUserById(userDetails.getId());
        Task task = taskService.save(saveTaskRequset, user);

        return ResponseEntity.ok().body(task);

    }

    @GetMapping
    public ResponseEntity<List<Task>> findAllByUser(@CurrentUser UserDetailsImpl userDetails) {
        User user = userService.getUserById(userDetails.getId());

        List<Task> tasks = taskService.findAllByUserEmail(user.getEmail());

        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable String id) {
        Task task;
        try {
            task = taskService.getTaskById(Long.parseLong(id));
        } catch (TaskNotFoundExeption e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(task);
    }
}

