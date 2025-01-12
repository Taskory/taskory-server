package codearchitect99.taskory.user;

import codearchitect99.taskory.user.model.SocialAccount;
import codearchitect99.taskory.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    boolean existsByUsername(String username);

    SocialAccount findByUsername(String username);

    void deleteByUser(User user);
}
