package codeartitect.taskflower.event;

import codeartitect.taskflower.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);

    void deleteAllByUser(User user);
}
