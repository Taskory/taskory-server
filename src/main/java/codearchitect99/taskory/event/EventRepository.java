package codearchitect99.taskory.event;

import codearchitect99.taskory.tag.model.Tag;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);

    void deleteAllByUser(User user);

    @Query("SELECT e FROM Event e WHERE e.startDateTime < :endDateTime AND e.dueDateTime > :startDateTime AND e.user = :user")
    List<Event> findAllByUserInPeriod(@Param("user") User user, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    List<Event> findByTag_IdIn(List<Long> tagIds);

    @Query("SELECT e FROM Event e WHERE " +
            "((e.startDateTime < :currentDate AND e.dueDateTime > :currentDate) " +
            "OR e.startDateTime > :currentDate)" +
            "AND e.user = :user")
    List<Event> findOngoingAndUpcommingEvents(@Param("user") User user, @Param("currentDate") LocalDateTime currentDate);

    List<Event> findByTag(Tag tag);
}
