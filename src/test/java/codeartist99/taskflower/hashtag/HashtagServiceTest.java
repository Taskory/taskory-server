package codeartist99.taskflower.hashtag;

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
class HashtagServiceTest {
    @Autowired
    private HashtagService hashtagService;

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
        user = User.builder()
                .username(username)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving hashtag
     */
    @Test
    @DisplayName("save hashtag and get hashtag test")
    void saveHashtag() throws HashtagNotFoundException {
//        Arrange
        String title = "test hashtag";
        SaveHashtagRequest saveHashtagRequest = new SaveHashtagRequest(title);

//        Act
        HashtagResponse hashtagResponse = hashtagService.save(user, saveHashtagRequest);

//        Assert
        assertEquals(hashtagResponse.toString(), hashtagService.getById(hashtagResponse.getId()).toString());
    }

    /**
     * Test for finding all hashtags by user
     */
    @Test
    @DisplayName("find all hashtags by user test")
    void findAllHashtags() {
//        Arrange
//        the first hashtag
        String title = "test hashtag";
        SaveHashtagRequest saveHashtagRequest = new SaveHashtagRequest(title);

//        the second hashtag
        String title2 = "test hashtag2";
        SaveHashtagRequest saveHashtagRequest2 = new SaveHashtagRequest(title2);

//        save the first hashtag
        HashtagResponse hashtagResponse = hashtagService.save(user, saveHashtagRequest);
//        save the second hashtag
        HashtagResponse hashtagResponse2 = hashtagService.save(user, saveHashtagRequest2);

//        Act
//        find all hashtags
        List<HashtagResponse> hashtagResponseList = hashtagService.findAll(user);

//        Assert
//        find the first hashtag
        HashtagResponse actualHashtagResponse = hashtagResponseList.get(0);
//        find the second hashtag
        HashtagResponse actualHashtagResponse2 = hashtagResponseList.get(1);

        assertEquals(hashtagResponse.toString(), actualHashtagResponse.toString());
        assertEquals(hashtagResponse2.toString(), actualHashtagResponse2.toString());
    }

    /**
     * Test for updating a hashtag
     */
    @Test
    @DisplayName("update a hashtag")
    void updateHashtag() throws HashtagNotFoundException {
//        Arrange
//        save a hashtag
        String title = "test hashtag";
        SaveHashtagRequest saveHashtagRequest = new SaveHashtagRequest(title);

        HashtagResponse hashtagResponse = hashtagService.save(user, saveHashtagRequest);

//        update the hashtag
        String updatedTitle = "updated hashtag";
        SaveHashtagRequest updateHashtagRequest = new SaveHashtagRequest(updatedTitle);

//        Act
        HashtagResponse updatedHashtagResponse = hashtagService.updateHashtag(hashtagResponse.getId(), updateHashtagRequest);

//        Assert
        assertEquals(updatedHashtagResponse.toString(), hashtagService.getById(hashtagResponse.getId()).toString());
    }

    /**
     * Test for deleting a hashtag
     */
    @Test
    @DisplayName("delete a hashtag")
    void deleteHashtagById() throws HashtagNotFoundException {
//        Arrange
//        save a hashtag
        String title = "test hashtag";
        SaveHashtagRequest saveHashtagRequest = new SaveHashtagRequest(title);

        HashtagResponse hashtagResponse = hashtagService.save(user, saveHashtagRequest);

        Long hashtagId = hashtagResponse.getId();

//        Act
        hashtagService.deleteById(hashtagId);

//        Assert
        assertThrows(HashtagNotFoundException.class, () -> hashtagService.getById(hashtagId));
    }
}
