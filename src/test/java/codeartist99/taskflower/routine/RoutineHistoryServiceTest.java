//package codeartist99.taskflower.routine;
//
//import codeartist99.taskflower.routine.exception.RoutineNotFoundException;
//import codeartist99.taskflower.routine.model.Routine;
//import codeartist99.taskflower.routine.model.RoutineHistory;
//import codeartist99.taskflower.routine.payload.RoutineHistoryResponse;
//import codeartist99.taskflower.routine.repository.RoutineHistoryRepository;
//import codeartist99.taskflower.routine.repository.RoutineRepository;
//import codeartist99.taskflower.routine.service.RoutineHistoryService;
//import codeartist99.taskflower.user.UserRepository;
//import codeartist99.taskflower.user.UserService;
//import codeartist99.taskflower.user.model.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class RoutineHistoryServiceTest {
//    private User user;
//    private Routine routine;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoutineHistoryService routineHistoryService;
//    @Autowired
//    private RoutineHistoryRepository routineHistoryRepository;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RoutineRepository routineRepository;
//
//    @BeforeEach
//    void setUp() {
//        StringBuilder tempUsername;
//        do {
//            tempUsername = new StringBuilder();
//            Random random = new Random();
//            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
//            for (int i = 0; i < 10; i++) {
//                tempUsername.append(charsForRandom[random.nextInt(36)]);
//            }
//        } while (userRepository.existsByUsername(tempUsername.toString()));
//        String username = tempUsername.toString();
//        user = User.builder()
//                .username(username)
//                .build();
//        userRepository.save(user);
//
//        routine = Routine.builder()
//                .title("test routine")
//                .days(new boolean[]{true, true, true, true, true, true, true})
//                .description("test description")
//                .user(user)
//                .build();
//        routineRepository.save(routine);
//    }
//
//    @AfterEach
//    void end() {
//        userService.deleteById(user.getId());
//    }
//
//
//    /**
//     * Test for check routine
//     */
//    @Test
//    @DisplayName("check routine")
//    void checkRoutine() throws RoutineNotFoundException {
////        Act
//        RoutineHistoryResponse routineHistoryResponse = routineHistoryService.checkRoutine(user, routine.getId());
//
////        Assert
//        RoutineHistory savedRoutineHistory = routineHistoryRepository.findById(routineHistoryResponse.getId()).orElseThrow();
//
//        assertEquals(routineHistoryResponse.toString(), new RoutineHistoryResponse(savedRoutineHistory).toString());
//
////        Delete history
//        routineHistoryRepository.deleteById(savedRoutineHistory.getId());
//    }
//
//    /**
//     * Test for uncheck routine
//     */
//    @Test
//    @DisplayName("uncheck routine")
//    void uncheckRoutine() throws RoutineNotFoundException {
////        Arrange
//        RoutineHistoryResponse routineHistoryResponse = routineHistoryService.checkRoutine(user, routine.getId());
//
////        Act
//        routineHistoryService.uncheckRoutine(routineHistoryResponse.getId());
//
////        Assert
//        assertEquals(routineHistoryRepository.findById(routineHistoryResponse.getId()), Optional.empty());
//    }
//
//}