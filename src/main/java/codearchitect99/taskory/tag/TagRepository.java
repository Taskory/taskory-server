package codearchitect99.taskory.tag;

import codearchitect99.taskory.tag.model.Tag;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUser(User user);

    void deleteAllByUser(User user);
}
