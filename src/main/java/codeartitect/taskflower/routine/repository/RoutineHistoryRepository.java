package codeartitect.taskflower.routine.repository;

import codeartitect.taskflower.routine.model.RoutineHistory;
import codeartitect.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineHistoryRepository extends JpaRepository<RoutineHistory, Long> {
    void deleteAllByUser(User user);
}
