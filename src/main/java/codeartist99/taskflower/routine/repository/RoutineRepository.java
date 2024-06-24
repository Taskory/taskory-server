package codeartist99.taskflower.routine.repository;

import codeartist99.taskflower.routine.model.Routine;
import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Optional<Routine>> findAllByUser(User user);

    void deleteAllByUser(User user);
}
