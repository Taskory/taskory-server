package taskflower.taskflower.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflower.taskflower.security.oauth2.model.OAuth2UserInfo;
import taskflower.taskflower.user.SocialAccount.SocialAccount;
import taskflower.taskflower.user.SocialAccount.SocialAccountRepository;
import taskflower.taskflower.user.SocialAccount.SocialProvider;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.exception.UserNotFoundException;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialAccountRepository socialAccountRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialAccountRepository socialAccountRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.socialAccountRepository = socialAccountRepository;
        this.userMapper = userMapper;
    }

    public boolean existsUser(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public UserDto signup(User signupUser) throws UserAlreadyExistedException {
        if (userRepository.existsUserByEmail(signupUser.getEmail())) {
            throw new UserAlreadyExistedException("Sorry, this email is Already existed..");
        }
        signupUser.setPassword(passwordEncoder.encode(signupUser.getPassword()));
        User user = userRepository.save(signupUser);
        return userMapper.convertUserToUserDto(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public UserDto updateUser(long id, User updateUser) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.setName(updateUser.getName());
        user.setEmail(updateUser.getEmail());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        User updatedUser = userRepository.save(user);
        return userMapper.convertUserToUserDto(updatedUser);
    }

    public void deleteById(long id) {
        // 삭제 성공 시, 결과 반환x, 실패할 경우 예외 처리
        // Task 테이블과 연관되기 때문에 로직 수정 필요!!!!
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

//    <<권한을 통해 사용자 정보을 읽어오는 것은 JWT 토큰으로 대체>>
//    public User findUserByAuth() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        return userRepository.findUserByEmail(principal.getEmail());
//    }

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return userMapper.convertUserToUserDto(user);
    }

    public User signupTempUserWithOAuth2(OAuth2UserInfo oAuth2UserInfo, String socialProvider) {
        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setSubId(oAuth2UserInfo.getSubId());
        socialAccount.setUsername(oAuth2UserInfo.getEmail());
        socialAccount.setName(oAuth2UserInfo.getName());
        socialAccount.setProvider(SocialProvider.valueOf(socialProvider));
        socialAccountRepository.save(socialAccount);

        User tempUser = new User();
        tempUser.setSocialAccount(Set.of(socialAccount));
        tempUser.setName(oAuth2UserInfo.getName());
        tempUser.setEmail(oAuth2UserInfo.getEmail());
        tempUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        tempUser.setLocalSignup(false);
        userRepository.save(tempUser);

        socialAccount.setUser(tempUser);
        socialAccountRepository.save(socialAccount);

        return tempUser;
    }
}
