package codeartitect.taskflower.routine;

import codeartitect.taskflower.routine.exception.InvalidDaysException;
import codeartitect.taskflower.routine.exception.RoutineNotFoundException;
import codeartitect.taskflower.routine.payload.RoutineResponse;
import codeartitect.taskflower.routine.payload.SaveRoutineRequest;
import codeartitect.taskflower.routine.service.RoutineService;
import codeartitect.taskflower.tag.TagNotFoundException;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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
    void setUp() {
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
        String zoneId = "Asia/Seoul";
        user = User.builder()
                .username(username)
                .zoneId(zoneId)
                .build();
        userRepository.save(user);
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
        boolean[] days = {false, false, false, false, false, false, false};
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
        boolean[] days = {false, false, false, false, false};
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
        boolean[] days = {false, false, false, false, false, false, false};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

//        the second routine
        String title2 = "test tile2";
        String description2 = "test description2";
        boolean[] days2 = {false, false, false, false, false, false, false};
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
     * Test for find today's routines
     */
    @Test
    @DisplayName("find all today's routine")
    void findAllToday() throws InvalidDaysException {
//        Arrange
//        not today's routine
        String title = "test tile";
        String description = "test description";
        boolean[] days = {false, false, false, false, false, false, false};
        SaveRoutineRequest notRoutine = new SaveRoutineRequest(title, description, days);

//        today's routine
        String title2 = "test tile2";
        String description2 = "test description2";
        boolean[] days2 = {false, false, false, false, false, false, false};
        int today = LocalDateTime.now().getDayOfWeek().getValue() - 1;
        days2[today] = true;
        SaveRoutineRequest correctRoutine = new SaveRoutineRequest(title2, description2, days2);

//        save a first routine
        RoutineResponse notRoutineResponse = routineService.save(user, notRoutine);
//        save a second routine
        RoutineResponse correctRoutineResponse = routineService.save(user, correctRoutine);

//        Act
//        find all today routines
        List<RoutineResponse> routineResponseList = routineService.findAllToday(user);
        RoutineResponse actualRoutineResponse = routineResponseList.get(0);

//        Assert
//        find a first routine
        assertEquals(correctRoutineResponse.toString(), actualRoutineResponse.toString());
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
        boolean[] days = {false, false, false, false, false, false, false};
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
        boolean[] days = {false, false, false, false, false, false, false};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

//        Act, Assert
//        update a routine
        boolean[] updateDays = {false, false, false, false, false};
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
        boolean[] days = {false, false, false, false, false, false, false};
        SaveRoutineRequest saveRoutineRequest = new SaveRoutineRequest(title, description, days);

        RoutineResponse routineResponse = routineService.save(user, saveRoutineRequest);

        Long routineId = routineResponse.getId();

//        Act
        routineService.deleteById(routineResponse.getId());

//        Assert
        assertThrows(RoutineNotFoundException.class, () -> routineService.getById(routineId));
    }

}