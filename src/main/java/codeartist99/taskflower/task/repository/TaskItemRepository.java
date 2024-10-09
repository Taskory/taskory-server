package codeartist99.taskflower.task.repository;

import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findAllByTask(Task task);

    void deleteAllByTask(Task task);
}
