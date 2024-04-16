package taskflower.taskflower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskflower.taskflower.model.entity.Tag;
import taskflower.taskflower.model.entity.User;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUser(User user);

    boolean existsByNameAndUser(String tagName, User user);
}
