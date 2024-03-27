package taskflower.taskflower.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ConnectionInfoRepository extends JpaRepository<OAuth2ConnectionInfo, Long> {
    OAuth2ConnectionInfo findByUsername(String username);

    boolean existsByUsername(String username);

}
