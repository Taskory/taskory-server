package taskflower.taskflower.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskflower.taskflower.task.model.Task;
import taskflower.taskflower.user.model.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    boolean existsByTitleAndUser(String title, User user);
}
