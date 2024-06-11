package codeartitect.taskflower.routine;

import codeartitect.taskflower.tag.TagNotFoundException;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.model.User;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.payload.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoutineServiceTest {
    @Autowired
    private RoutineService routineService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User user;

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, InvalidZoneIdException {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        String username = tempUsername.toString();
        String password = "1234";
        String zoneId = "Asia/Seoul";
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);
        user = userRepository.findById(userResponse.getId()).orElseThrow();
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving routine
     */
    @Test
    @DisplayName("save routine and get routine test")
    void save() throws InvalidDaysException, RoutineNotFoundException {
//        Arrange
        String title = "test tile";
        String description = "test description";
        byte[] days = {1, 1, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

//        Act
        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

//        Assert
        assertEquals(routineResponse.toString(), routineService.getById(routineResponse.getId()).toString());
    }

    /**
     * Test for invalid routine days
     */
    @Test
    @DisplayName("save invalid routine days")
    void save_invalid_days() {
//        Arrange
        String title = "test tile";
        String description = "test description";
        byte[] days = {1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

//        Act, Assert
        assertThrows(InvalidDaysException.class, () -> routineService.save(user, saveRoutineRequest));
    }

    /**
     * Test for find all tags by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() throws InvalidDaysException {
//        Arrange
//        the first routine
        String title = "test tile";
        String description = "test description";
        byte[] days = {1, 1, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

//        the second routine
        String title2 = "test tile2";
        String description2 = "test description2";
        byte[] days2 = {0, 0, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest2 = new SaveRoutineRequest(title2, description2, days2);

//        save a first routine
        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);
//        save a second routine
        RoutineResponse routineResponse2 = routineService.save(user, saveRoutineRequest2);

//        Act
//        find all routines
        List<RoutineResponse> routineResponseList = routineService.findAll(user);

//        Assert
//        find a first routine
        RoutineResponse actualRoutineResponse = routineResponseList.get(0);
//        find a second routine
        RoutineResponse actualRoutineResponse2 = routineResponseList.get(1);

        assertEquals(routineResponse.toString(), actualRoutineResponse.toString());
        assertEquals(routineResponse2.toString(), actualRoutineResponse2.toString());
    }

    /**
     * Test for update a routine
     */
    @Test
    @DisplayName("update a routine")
    void updateRoutine() throws InvalidDaysException, TagNotFoundException, RoutineNotFoundException {
//        Arrange
//        to save a routine
        String title = "test tile";
        String description = "test description";
        byte[] days = {1, 1, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

//        update a routine
        String updateTitle = "test tile2";
        SaveRoutineRequest updateRoutineRequest = new SaveRoutineRequest(updateTitle, description, days);

//        Act
        RoutineResponse updateRoutineResponse = routineService.updateRoutine(routineResponse.getId(), updateRoutineRequest);

//        Assert
        assertEquals(updateRoutineResponse.toString(), routineService.getById(routineResponse.getId()).toString());
    }

    /**
     * Test for update invalid routine days
     */
    @Test
    @DisplayName("update invalid routine days")
    void update_invalid_days() throws InvalidDaysException {
//        Arrange
//        to save a routine
        String title = "test tile";
        String description = "test description";
        byte[] days = {1, 1, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

//        Act, Assert
//        update a routine
        byte[] updateDays = {1, 1, 1};
        SaveRoutineRequest updateRoutineRequest = new SaveRoutineRequest(title, description, updateDays);
        assertThrows(InvalidDaysException.class, () -> routineService.updateRoutine(routineResponse.getId(), updateRoutineRequest)) ;

    }

    /**
     * Test for delete a routine
     */
    @Test
    @DisplayName("delete routine")
    void deleteById() throws InvalidDaysException, RoutineNotFoundException {
//        Arrange
//        save a routine
        String title = "test tile";
        String description = "test description";
        byte[] days = {1, 1, 1, 1, 1, 1, 1};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

        Long routineId = routineResponse.getId();

//        Act
        routineService.deleteById(routineResponse.getId());

//        Assert
        assertThrows(RoutineNotFoundException.class, () -> routineService.getById(routineId));
    }

}