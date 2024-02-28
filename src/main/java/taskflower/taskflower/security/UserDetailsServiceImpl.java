package taskflower.taskflower.security;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userService.findUserByEmail(email);
        return new UserDetailsImpl(user);
    }

    @Transactional
    public UserDetails loadUserById(long userId) {
        User user = userService.getUserById(userId);
        return new UserDetailsImpl(user);
    }
}
