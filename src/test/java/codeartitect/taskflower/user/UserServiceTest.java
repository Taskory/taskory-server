package codeartitect.taskflower.user;

import codeartitect.taskflower.user.dto.UserResponse;
import codeartitect.taskflower.user.dto.UserSignupRequest;
import codeartitect.taskflower.user.dto.UserUpdateRequest;
import codeartitect.taskflower.user.exception.InvalidUsernameException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    String username;
    String password;
    String zoneId;

    @BeforeEach
    void setUp() {
        username = getUsername();
        password = "1234";
        zoneId = "Asia/Seoul";
    }

    @AfterEach
    void end() {
        if (userRepository.existsByUsername(username)) {
            userService.deleteById(userService.getByUsername(username).getId());
        }
    }

    /**
     * @return Random username
     */
    private String getUsername() {
        StringBuilder username;
        do {
            username = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                username.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(username.toString()));
        return username.toString();
    }

    /**
     * Test for signup
     * Valid user
     */
    @Test
    @DisplayName("Signup test and Get user")
    void signup_getByUsername() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);

//        Act
        UserResponse userResponse = userService.signup(userSignupRequest);

//        Assert
        UserResponse signupUser = userService.getByUsername(username);

        assertEquals(userResponse.toString(), signupUser.toString());
    }

    /**
     * Test for failed signup test
     * Invalid username
     */
    @Test
    @DisplayName("Invalid username signup")
    void signup_invalid_username() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);

//        Act
        userService.signup(userSignupRequest);

//        Assert
        assertThrows(InvalidUsernameException.class, () -> userService.signup(userSignupRequest));
    }

    /**
     * Test for failed signup test
     * Invalid zone id
     */
    @Test
    @DisplayName("Invalid zone id signup")
    void signup_invalid_zoneId() {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, "invalid zone");

//        Act and Assert
        assertThrows(InvalidZoneIdException.class, () -> userService.signup(userSignupRequest));
    }

    /**
     * Test for update user
     * Valid user
     */
    @Test
    @DisplayName("Update user")
    void updateUser() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(userSignupRequest);

//        Act
        String updateUsername = getUsername();
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(userResponse.getId(), updateUsername, "4321", "Asia/Seoul");
        UserResponse updatedUserResponse = userService.updateUser(userUpdateRequest);

//        Assert
        assertEquals(updatedUserResponse.toString(), userService.getByUsername(updateUsername).toString());

//        For AfterEach
        this.username = updateUsername;
    }

    /**
     * Test for failed user update
     * Invalid username
     */
    @Test
    @DisplayName("Invalid username  update user")
    void updateUser_invalid_username() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
//        first user
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(userSignupRequest);
//        second user
        String invalidUsername = "invalidUsername";
        UserSignupRequest duplicatedUser = new UserSignupRequest(invalidUsername, password, zoneId);
        UserResponse duplicatedUserResponse = userService.signup(duplicatedUser);

//        Act and Assert
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(userResponse.getId(), invalidUsername, "4321", "Asia/Seoul");
        assertThrows(InvalidUsernameException.class, () -> userService.updateUser(userUpdateRequest));

//        duplicatedUser delete
        userService.deleteById(duplicatedUserResponse.getId());
    }

    /**
     * Test for faild user update
     * Invalid zone id
     */
    @Test
    @DisplayName("Invalid zone id update user")
    void updateUser_invalid_zoneId() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(userSignupRequest);

//        Act and Assert
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(userResponse.getId(), userResponse.getUsername(), "4321", "Invalid zone id");
        assertThrows(InvalidZoneIdException.class, () -> userService.updateUser(userUpdateRequest));
    }

    /**
     * Test for delete user
     */
    @Test
    @DisplayName("Delete user")
    void deleteById() throws InvalidUsernameException, InvalidZoneIdException {
//        Arrange
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(userSignupRequest);

//        Act
        userService.deleteById(userResponse.getId());

//        Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(username));
    }
}