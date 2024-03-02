package taskflower.taskflower.security;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserRepository;
<<<<<<< HEAD
=======
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserNotFoundException;
>>>>>>> ca6424c (feat: 각 요청에 대해 TokenFilter 기능 추가)

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

<<<<<<< HEAD
    public UserDetailServiceImpl(UserRepository userRepository) {
=======
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
>>>>>>> ca6424c (feat: 각 요청에 대해 TokenFilter 기능 추가)
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
<<<<<<< HEAD
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email" + email);
        }

        return new UserDetailsImpl(user);
    }

=======
        return new UserDetailsImpl(user);
    }

    @Transactional
    public UserDetails loadUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserDetailsImpl(user);
    }
>>>>>>> ca6424c (feat: 각 요청에 대해 TokenFilter 기능 추가)
}
