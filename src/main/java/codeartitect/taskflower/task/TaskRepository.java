package codeartitect.taskflower.task;

import codeartitect.taskflower.event.Event;
import codeartitect.taskflower.flow.Flow;
import codeartitect.taskflower.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    List<Task> findAllByUserAndEvent(User user, Event event);

    List<Task> findAllByUserAndFlow(User user, Flow flow);

    List<Task> findAllByUserAndFlowAndEvent(User user, Flow flow, Event event);
}
