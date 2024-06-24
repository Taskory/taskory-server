package codeartist99.taskflower.routine.repository;

import codeartist99.taskflower.routine.model.RoutineHistory;
import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineHistoryRepository extends JpaRepository<RoutineHistory, Long> {
    void deleteAllByUser(User user);
}
