package codeartitect.taskflower.user;

import codeartitect.taskflower.user.payload.ProfileUpdateRequest;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.ZoneId;
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
        StringBuilder stringBuilder;
        do {
            stringBuilder = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                stringBuilder.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(stringBuilder.toString()));
        return stringBuilder.toString();
    }

    /**
     * Test for signup
     * Valid user
     */
    @Test
    @DisplayName("signup test and Get user")
    @Deprecated
    @Disabled
    void signup_getByUsername() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);

//        Act
        UserResponse userResponse = userService.signup(signupRequest);

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
    @Deprecated
    @Disabled
    void signup_invalid_username() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);

//        Act
        userService.signup(signupRequest);

//        Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.signup(signupRequest));
    }

    /**
     * Test for failed signup test
     * Invalid zone id
     */
    @Test
    @DisplayName("Invalid zone id signup")
    @Deprecated
    @Disabled
    void signup_invalid_zoneId() {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, "invalid zone");

//        Act and Assert
        assertThrows(InvalidZoneIdException.class, () -> userService.signup(signupRequest));
    }

    /**
     * Test for update user profile
     * Valid user
     */
    @Test
    @DisplayName("Update user profile")
    void updateProfile() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);


//        Act
        String updateUsername = getUsername();
        String updatedZoneId = ZoneId.of("Asia/Tokyo").toString();
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(updateUsername, updatedZoneId);
        UserResponse updatedUserResponse = userService.updateProfile(userResponse.getId(), profileUpdateRequest);

//        Assert
        assertEquals(updatedUserResponse.toString(), userService.getByUsername(updateUsername).toString());

//        For AfterEach
        this.username = updateUsername;
    }

    /**
     * Test for fail user profile update
     * Invalid username
     */
    @Test
    @DisplayName("Invalid username update user")
    void updateUser_invalid_username() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
//        first user
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);
//        second user
        String invalidUsername = getUsername();
        SignupRequest duplicatedUser = new SignupRequest(invalidUsername, password, zoneId);
        UserResponse duplicatedUserResponse = userService.signup(duplicatedUser);

//        Act and Assert
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(invalidUsername, "Asia/Seoul");
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.updateProfile(userResponse.getId(), profileUpdateRequest));

//        duplicatedUser delete
        userService.deleteById(duplicatedUserResponse.getId());
    }

    /**
     * Test for fail user profile update
     * Invalid zone id
     */
    @Test
    @DisplayName("Invalid zone id update user")
    void updateUser_invalid_zoneId() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);

//        Act and Assert
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(userResponse.getUsername(), "Invalid zone id");
        assertThrows(InvalidZoneIdException.class, () -> userService.updateProfile(userResponse.getId(), profileUpdateRequest));
    }

    /**
     * Test for delete user
     */
    @Test
    @DisplayName("Delete user")
    void deleteById() throws UsernameAlreadyExistsException, InvalidZoneIdException {
//        Arrange
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);

//        Act
        userService.deleteById(userResponse.getId());

//        Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(username));
    }
}