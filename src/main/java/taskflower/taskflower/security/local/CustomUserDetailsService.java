package taskflower.taskflower.security.local;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.UserPrincipal;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.UserRepository;

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
        User user = userRepository.findById(userId).orElseThrow();
        return new UserPrincipal(user);
    }
}
