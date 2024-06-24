package codeartist99.taskflower.event;

import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Optional<Event>> findAllByUser(User user);

    void deleteAllByUser(User user);
}
