package codeartist99.taskflower.task.repository;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    List<Task> findAllByUserAndEvent(User user, Event event);

    void deleteAllByUser(User user);

    List<Task> findByTag_IdIn(List<Long> tagIds);
}
