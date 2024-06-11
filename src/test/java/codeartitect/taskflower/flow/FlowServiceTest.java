package codeartitect.taskflower.flow;

import codeartitect.taskflower.flow.payload.FlowResponse;
import codeartitect.taskflower.flow.payload.SaveFlowRequest;
import codeartitect.taskflower.hashtag.Hashtag;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.model.User;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlowServiceTest {

    @Autowired
    private FlowService flowService;

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
     * Test for saving flow
     */
    @Test
    @DisplayName("save flow and get flow test")
    void save() {
//        Arrange
        String title = "test tile";
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveFlowRequest saveFlowRequest = new SaveFlowRequest(title, hashtags, description);

//        Act
        FlowResponse flowResponse = flowService.save(user, saveFlowRequest);

//        Assert
        assertEquals(flowResponse.toString(), flowService.getById(flowResponse.getId()).toString());
    }

    /**
     * Test for find all flows by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() {
//        Arrange
//        the first flow
        String title = "test tile";
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveFlowRequest saveFlowRequest = new SaveFlowRequest(title, hashtags, description);

//        the second flow
        String title2 = "test tile2";
        List<Hashtag> hashtags2 = null;
        String description2 = "test description2";
        SaveFlowRequest saveFlowRequest2 = new SaveFlowRequest(title2, hashtags2, description2);

//        save a first flow
        FlowResponse flowResponse = flowService.save(user, saveFlowRequest);
//        save a second flow
        FlowResponse flowResponse2 = flowService.save(user, saveFlowRequest2);

//        Act
//        find all flows
        List<FlowResponse> flowResponseList = flowService.findAll(user);

//        Assert
//        find a first flow
        FlowResponse actualFlowResponse = flowResponseList.get(0);
//        find a second flow
        FlowResponse actualFlowResponse2 = flowResponseList.get(1);

        assertEquals(flowResponse.toString(), actualFlowResponse.toString());
        assertEquals(flowResponse2.toString(), actualFlowResponse2.toString());
    }

    /**
     * Test for update a flow
     */
    @Test
    @DisplayName("update an flow")
    void updateTask() {
//        Arrange
//        to save a flow
        String title = "test tile";
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveFlowRequest saveFlowRequest = new SaveFlowRequest(title, hashtags, description);;

        FlowResponse flowResponse = flowService.save(user, saveFlowRequest);

//        update a flow
        String updateTitle = "update title";
        List<Hashtag> updateHashtags = null;
        String updateDescription = "test description";
        SaveFlowRequest updateFlowRequest = new SaveFlowRequest(updateTitle, updateHashtags, updateDescription);

//        Act
        FlowResponse updateFlowResponse = flowService.updateFlow(flowResponse.getId(), updateFlowRequest);

//        Assert
        assertEquals(updateFlowResponse.toString(), flowService.getById(flowResponse.getId()).toString());
    }

    /**
     * Test for delete a flow
     */
    @Test
    @DisplayName("delete flow")
    void deleteById() {
//        Arrange
//        save a task
        String title = "test title";
        List<Hashtag> hashtags = null;
        String description = "test description";
        SaveFlowRequest saveFlowRequest = new SaveFlowRequest(title, hashtags, description);

        FlowResponse flowResponse = flowService.save(user, saveFlowRequest);

        Long flowId = flowResponse.getId();

//        Act
        flowService.deleteById(flowResponse.getId());

//        Assert
        assertThrows(FlowNotFoundException.class, () -> flowService.getById(flowId));
    }
}