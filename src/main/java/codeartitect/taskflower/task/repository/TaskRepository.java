package codeartitect.taskflower.task.repository;

import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.task.model.Task;
import codeartitect.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Optional<Task>> findAllByUser(User user);

    List<Optional<Task>> findAllByUserAndEvent(User user, Event event);

    List<Optional<Task>> findAllByUserAndFlow(User user, Flow flow);

    List<Optional<Task>> findAllByUserAndFlowAndEvent(User user, Flow flow, Event event);

    void deleteAllByUser(User user);
}
