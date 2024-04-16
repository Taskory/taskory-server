package taskflower.taskflower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskflower.taskflower.model.entity.Task;
import taskflower.taskflower.model.entity.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    boolean existsByTitleAndUser(String title, User user);
}
