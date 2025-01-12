package codearchitect99.taskory.task.repository;

import codearchitect99.taskory.event.Event;
import codearchitect99.taskory.tag.model.Tag;
import codearchitect99.taskory.task.model.Task;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    List<Task> findAllByUserAndEvent(User user, Event event);

    void deleteAllByUser(User user);

    List<Task> findByTag_IdIn(List<Long> tagIds);

    List<Task> findByTag(Tag tag);

    List<Task> findByEvent(Event event);
}
