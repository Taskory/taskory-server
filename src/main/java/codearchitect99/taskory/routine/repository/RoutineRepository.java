package codearchitect99.taskory.routine.repository;

import codearchitect99.taskory.routine.model.Routine;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Optional<Routine>> findAllByUser(User user);

    void deleteAllByUser(User user);
}
