package codeartist99.taskflower.tag;

import codeartist99.taskflower.tag.model.Color;
import codeartist99.taskflower.tag.payload.SaveTagRequest;
import codeartist99.taskflower.tag.payload.TagResponse;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.UserService;
import codeartist99.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
     * Test for saving tag
     */
    @Test
    @DisplayName("save tag and get tag test")
    void save() throws TagNotFoundException {
//        Arrange
        String title = "test tile";
        SaveTagRequest saveTagRequest = new SaveTagRequest(title, Color.BLUE);

//        Act
        TagResponse tagResponse = tagService.save(user, saveTagRequest);

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
    void updateTask() throws TagNotFoundException {
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
    void deleteById() throws TagNotFoundException {
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