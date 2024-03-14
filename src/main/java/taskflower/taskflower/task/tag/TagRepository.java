package taskflower.taskflower.task.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskflower.taskflower.user.User;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUser(User user);

    boolean existsByNameAndUser(String tagName, User user);
}
