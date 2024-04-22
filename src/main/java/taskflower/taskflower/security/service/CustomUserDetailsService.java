package taskflower.taskflower.security.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.model.UserPrincipal;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.repository.UserRepository;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(long userId) {
        log.info("[LOG - CustomUserDetailsService.loadUserById]");
        User user = userRepository.findById(userId).orElseThrow();
        log.info("[LOG - CustomUserDetailsService.loadUserById] user : {}", user);

        return new UserPrincipal(user);
    }
}
