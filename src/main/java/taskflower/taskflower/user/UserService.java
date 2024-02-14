package taskflower.taskflower.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.exception.UserNotFoundException;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void signup(User signupUser) {
        signupUser.setPassword(encode(signupUser.getPassword()));
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
        user.setPassword(encode(updatedUser.getPassword()));

        userRepository.save(user);
    }

    public void deleteById(long id) {
        // 삭제 성공 시, 결과 반환x, 실패할 경우 예외 처리
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
