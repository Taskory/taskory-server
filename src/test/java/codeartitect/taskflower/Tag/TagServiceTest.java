package codeartitect.taskflower.Tag;

import codeartitect.taskflower.flow.FlowNotFoundException;
import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.dto.UserResponse;
import codeartitect.taskflower.user.dto.UserSignupRequest;
import codeartitect.taskflower.user.entity.User;
import codeartitect.taskflower.user.exception.InvalidUsernameException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
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
class TagServiceTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User user;

    @BeforeEach
    void setUp() throws InvalidUsernameException, InvalidZoneIdException {
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
        UserSignupRequest userSignupRequest = new UserSignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(userSignupRequest);
        user = userRepository.findById(userResponse.getId()).orElseThrow();
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving tag
     */
    @Test
    @DisplayName("save tag and get tag test")
    void save() {
//        Arrange
        String title = "test tile";
        SaveTagRequest saveFlowRequest = new SaveTagRequest(title, Color.BLUE);

//        Act
        TagResponse tagResponse = tagService.save(user, saveFlowRequest);

//        Assert
        assertEquals(tagResponse.toString(), tagService.getById(tagResponse.getId()).toString());
    }

    /**
     * Test for find all tags by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() {
//        Arrange
//        the first tag
        String title = "test tile";
        SaveTagRequest saveTagRequest = new SaveTagRequest(title, Color.BLUE);

//        the second tag
        String title2 = "test tile2";
        SaveTagRequest saveTagRequest2 = new SaveTagRequest(title, Color.PINK);

//        save a first tag
        TagResponse tagResponse = tagService.save(user, saveTagRequest);
//        save a second tag
        TagResponse tagResponse2 = tagService.save(user, saveTagRequest2);

//        Act
//        find all tags
        List<TagResponse> tagResponseList = tagService.findAll(user);

//        Assert
//        find a first tag
        TagResponse actualTagResponse = tagResponseList.get(0);
//        find a second tag
        TagResponse actualTagResponse2 = tagResponseList.get(1);

        assertEquals(tagResponse.toString(), actualTagResponse.toString());
        assertEquals(tagResponse2.toString(), actualTagResponse2.toString());
    }

    /**
     * Test for update a tag
     */
    @Test
    @DisplayName("update a tag")
    void updateTask() {
//        Arrange
//        to save a tag
        String title = "test tile";
        SaveTagRequest saveTagRequest = new SaveTagRequest(title, Color.BLUE);

        TagResponse tagResponse = tagService.save(user, saveTagRequest);

//        update a tag
        String updateTitle = "test tile2";
        SaveTagRequest updateTagRequest = new SaveTagRequest(updateTitle, Color.CYAN);

//        Act
        TagResponse updateTagResponse = tagService.updateTag(tagResponse.getId(), updateTagRequest);

//        Assert
        assertEquals(updateTagResponse.toString(), tagService.getById(tagResponse.getId()).toString());
    }

    /**
     * Test for delete a tag
     */
    @Test
    @DisplayName("delete tag")
    void deleteById() {
//        Arrange
//        save a tag
        String title = "test tile";
        SaveTagRequest saveTagRequest = new SaveTagRequest(title, Color.BLUE);

        TagResponse tagResponse = tagService.save(user, saveTagRequest);

        Long tagId = tagResponse.getId();

//        Act
        tagService.deleteById(tagResponse.getId());

//        Assert
        assertThrows(TagNotFoundException.class, () -> tagService.getById(tagId));
    }
}