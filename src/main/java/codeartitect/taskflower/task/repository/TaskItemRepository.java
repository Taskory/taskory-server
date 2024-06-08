package codeartitect.taskflower.task.repository;

import codeartitect.taskflower.task.model.Task;
import codeartitect.taskflower.task.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {
    List<Optional<TaskItem>> findAllByTask(Task task);

    void deleteAllByTask(Task task);
}
