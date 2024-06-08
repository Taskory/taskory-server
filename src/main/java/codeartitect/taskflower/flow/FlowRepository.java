package codeartitect.taskflower.flow;

import codeartitect.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {
    List<Optional<Flow>> findAllByUser(User user);

    void deleteAllByUser(User user);
}
