package codeartitect.taskflower.task.taskitem;

import codeartitect.taskflower.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findAllByTask(Task task);

    void deleteAllByTask(Task task);
}
