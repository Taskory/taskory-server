package taskflower.taskflower.security;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
        return new UserDetailsImpl(user);
    }

    @Transactional
    public UserDetails loadUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserDetailsImpl(user);
    }
}
