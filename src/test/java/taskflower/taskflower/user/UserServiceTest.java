package taskflower.taskflower.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.exception.UserNotFoundException;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("사용자 생성 및 조회")
    void signup() throws Exception {
        UserDto user = createTestUser();

        UserDto signupUser = userMapper.convertUserToUserDto(userService.getUserById(user.getId()));

        assertEquals(user.toString(), signupUser.toString());
    }


    @Test
    @DisplayName("사용자 수정")
    void updateUser() throws Exception {
        UserDto user = createTestUser();
        long userId = user.getId();

        User updateUser = userService.getUserById(userId);
        updateUser.setName("test2");
        updateUser.setEmail("test1234@naver.com");
        updateUser.setPassword("4321");

        UserDto updatedUser = userService.updateUser(userId, updateUser);

        UserDto actualUser = userMapper.convertUserToUserDto(userService.getUserById(userId));
        assertEquals(updatedUser.toString(), actualUser.toString());
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteById() throws Exception {
        UserDto user = createTestUser();
        long userId = user.getId();

        userService.deleteById(userId);
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }

    private UserDto createTestUser() throws UserAlreadyExistedException {
        Random random = new Random();
        StringBuilder email = new StringBuilder();

        email.append((char) (random.nextInt(26) + 'a'));

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
            for (int i = 1; i < 10; i++) {
                email.append(characters.charAt(random.nextInt(characters.length())));
            }

        email.append("@example.com");

        User user = new User();
        user.setName("test");
        user.setEmail(email.toString());
        user.setPassword("1234");

        return userService.signup(user);
    }

//    <<권한을 통해 사용자 정보을 읽어오는 것은 JWT 토큰으로 대체>>
//    @Test
//    @DisplayName("로그인 및 로그인 유저 정보 읽어오기 테스트")
//    void findUserByAuthTest() throws Exception {
//        User user = createTestUser();
//
//        LoginRequset loginRequset = new LoginRequset();
//        loginRequset.setEmail(user.getEmail());
//        loginRequset.setPassword("1234");           // 회원가입 로직에서 비밀번호가 암호화되기 때문에 따로 설정
//
//        authService.authenticateUser(loginRequset);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User signupUser = userService.findUserByAuth();
//
//        Assertions.assertEquals(signupUser, user);
//    }
}