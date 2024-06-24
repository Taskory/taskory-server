package codeartist99.taskflower.task.repository;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.flow.Flow;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.user.model.User;
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
