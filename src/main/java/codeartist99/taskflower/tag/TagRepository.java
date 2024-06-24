package codeartist99.taskflower.tag;

import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Optional<Tag>> findAllByUser(User user);

    void deleteAllByUser(User user);
}
