package codearchitect99.taskory.routine.repository;

import codearchitect99.taskory.routine.model.RoutineHistory;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineHistoryRepository extends JpaRepository<RoutineHistory, Long> {
    void deleteAllByUser(User user);
}
