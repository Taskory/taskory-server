package taskflower.taskflower.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.exception.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsUser(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public User signup(User signupUser) throws UserAlreadyExistedException {
        if (userRepository.existsUserByEmail(signupUser.getEmail())) {
            throw new UserAlreadyExistedException("Sorry, this email is Already existed..");
        }
        signupUser.setPassword(passwordEncoder.encode(signupUser.getPassword()));
        return userRepository.save(signupUser);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User updateUser(long id, User updatedUser) {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void deleteById(long id) {
        // 삭제 성공 시, 결과 반환x, 실패할 경우 예외 처리
        // Task 테이블과 연관되기 때문에 로직 수정 필요!!!!
        User user = getUserById(id);
        userRepository.delete(user);
    }

//    <<권한을 통해 사용자 정보을 읽어오는 것은 JWT 토큰으로 대체>>
//    public User findUserByAuth() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        return userRepository.findUserByEmail(principal.getEmail());
//    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
