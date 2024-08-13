package codeartist99.taskflower.hashtag;

import codeartist99.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findAllByUser(User user);

    void deleteByUser(User user);
}
