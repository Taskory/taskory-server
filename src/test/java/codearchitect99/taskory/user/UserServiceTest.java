package codearchitect99.taskory.user;

import codearchitect99.taskory.user.exception.UsernameAlreadyExistsException;
import codearchitect99.taskory.user.model.User;
import codearchitect99.taskory.user.payload.ProfileUpdateRequest;
import codearchitect99.taskory.user.payload.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {

    private User user;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    /**
     * Common Arrange test
     */
    @BeforeEach
    void setUp() {
        String username = getUsername();
        String zoneId = "Asia/Seoul";
        user = User.builder()
                .username(username)
                .build();
        userRepository.save(user);
    }

    private String getUsername() {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        return tempUsername.toString();
    }

    @AfterEach
    void end() {
        if (userRepository.existsById(user.getId())) {
            userService.deleteById(user.getId());
        }
    }


    /**
     * Test for update user profile
     * Valid user
     */
    @Test
    @DisplayName("Update user profile")
    void updateProfile() throws UsernameAlreadyExistsException {
//        Arrange
        UserResponse savedUserResponse = new UserResponse(user);


//        Act
        String updateUsername = getUsername();
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(updateUsername);
        UserResponse updatedUserResponse = userService.updateProfile(savedUserResponse.getId(), profileUpdateRequest);

//        Assert
        assertEquals(updatedUserResponse.toString(), userService.getByUsername(updateUsername).toString());
    }

    /**
     * Test for fail user profile update
     * Invalid username
     */
    @Test
    @DisplayName("Invalid username update user")
    void updateUser_invalid_username() {
//        Arrange
//        first user
        UserResponse savedUserResponse = new UserResponse(user);
//        second user
        String invalidUsername = getUsername();
        User duplicatedUser = User.builder()
                .username(invalidUsername)
                .build();
        userRepository.save(duplicatedUser);
        UserResponse duplicatedUserResponse = new UserResponse(duplicatedUser);

//        Act and Assert
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(invalidUsername);
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.updateProfile(savedUserResponse.getId(), profileUpdateRequest));

//        duplicatedUser delete
        userService.deleteById(duplicatedUserResponse.getId());
    }

    /**
     * Test for delete user
     */
    @Test
    @DisplayName("Delete user")
    void deleteById() {
//        Arrange
        UserResponse savedUserResponse = new UserResponse(user);

//        Act
        userService.deleteById(savedUserResponse.getId());

//        Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(savedUserResponse.getUsername()));
    }



}