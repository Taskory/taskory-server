package taskflower.taskflower.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.UserDetailsImpl;
import taskflower.taskflower.user.exception.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(User signupUser) throws Exception {
        if (userRepository.existsByEmail(signupUser.getEmail())) {
            throw new Exception("Email address already exists");
        }
        signupUser.setPassword(passwordEncoder.encode(signupUser.getPassword()));
        userRepository.save(signupUser);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public void updateUser(long id, User updatedUser) {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userRepository.save(user);
    }

    public void deleteById(long id) {
        // 삭제 성공 시, 결과 반환x, 실패할 경우 예외 처리
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public User findUserByAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findUserByEmail(principal.getEmail());
    }
}
