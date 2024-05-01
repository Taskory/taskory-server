package taskflower.taskflower.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskflower.taskflower.model.enums.Role;
import taskflower.taskflower.security.model.OAuth2UserInfo;
import taskflower.taskflower.mapper.UserMapper;
import taskflower.taskflower.repository.UserRepository;
import taskflower.taskflower.payload.ProfileResponse;
import taskflower.taskflower.model.entity.SocialAccount;
import taskflower.taskflower.model.enums.SocialProvider;
import taskflower.taskflower.exception.UserAlreadyExistedException;
import taskflower.taskflower.exception.UserNotFoundException;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.model.dto.UserDto;

import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        signupUser.addRole(Role.USER);
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

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return userMapper.convertUserToUserDto(user);
    }

    public ProfileResponse getProfile(Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
            log.info("[LOG - UserService.getProfile()] user : {}", user);
            return userMapper.convertUserToProfileResponse(user);
        } else throw new UsernameNotFoundException("Not found user");
    }

    @Transactional
    public User signupTempUserWithOAuth2(OAuth2UserInfo oAuth2UserInfo, String socialProvider) {
        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setSubId(oAuth2UserInfo.getSubId());
        socialAccount.setUsername(oAuth2UserInfo.getEmail());
        socialAccount.setName(oAuth2UserInfo.getName());
        socialAccount.setProvider(SocialProvider.valueOf(socialProvider.toUpperCase()));

        User tempUser = new User();
        tempUser.setName(oAuth2UserInfo.getName());
        tempUser.setEmail(oAuth2UserInfo.getEmail());
        tempUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        tempUser.addSocialAccount(socialAccount);
        tempUser.addRole(Role.TEMP_USER);

        userRepository.save(tempUser);

        return tempUser;
    }

    /*
    * Temporary user -> Official user
    * 임시 계정을 정식 계정으로 가입
    * */
    public void signupWithOAuth2(User signupUser) {
        signupUser.setPassword(passwordEncoder.encode(signupUser.getPassword()));
        signupUser.initRole();
        signupUser.addRole(Role.USER);
        userRepository.save(signupUser);
    }
}
