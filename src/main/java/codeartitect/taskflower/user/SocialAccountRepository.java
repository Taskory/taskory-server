package codeartitect.taskflower.user;

import codeartitect.taskflower.user.model.SocialAccount;
import codeartitect.taskflower.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    boolean existsByUsername(String username);

    SocialAccount findByUsername(String username);

    void deleteByUser(User user);
}
