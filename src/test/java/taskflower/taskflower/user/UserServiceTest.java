package taskflower.taskflower.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("사용자 생성 및 조회")
    void signup() {
        User user = createTestUser();

        User signupUser = userService.getUserById(user.getId());

        assertEquals(signupUser, user);
    }


    @Test
    @DisplayName("사용자 수정")
    void updateUser() {
        User user = createTestUser();
        long userId = user.getId();

        User updateUser = userService.getUserById(userId);
        updateUser.setName("test2");
        updateUser.setEmail("test1234@naver.com");
        updateUser.setPassword("4321");

        userService.updateUser(userId, updateUser);
        updateUser.setPassword(userService.getUserById(userId).getPassword());

        assertEquals(updateUser, userService.getUserById(userId));
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteById() {
        User user = createTestUser();
        long userId = user.getId();

        userService.deleteById(userId);
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }

    private User createTestUser() {
        User user = new User();
        user.setName("test");
        user.setEmail("test1234@gmail.com");
        user.setPassword("1234");

        userService.signup(user);
        return user;
    }
}