package taskflower.taskflower.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    boolean existsByUsername(String username);

    SocialAccount findBySubId(String subId);
}
