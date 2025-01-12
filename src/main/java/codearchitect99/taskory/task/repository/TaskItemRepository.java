package codearchitect99.taskory.task.repository;

import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.task.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findAllByTask(Task task);

    void deleteAllByTaskIn(List<Task> tasks);

    List<TaskItem> findByTask(Task task);
}
