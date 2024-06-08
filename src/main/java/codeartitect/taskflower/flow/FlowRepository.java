package codeartitect.taskflower.flow;

import codeartitect.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {
    List<Flow> findAllByUser(User user);

    void deleteAllByUser(User user);
}
