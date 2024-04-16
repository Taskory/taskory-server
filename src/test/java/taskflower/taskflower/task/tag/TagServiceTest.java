package taskflower.taskflower.task.tag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskflower.taskflower.security.data.dto.SignupRequest;
import taskflower.taskflower.service.TagService;
import taskflower.taskflower.exception.TagExistException;
import taskflower.taskflower.exception.TagNotFoundException;
import taskflower.taskflower.model.dto.TagDto;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.service.UserService;
import taskflower.taskflower.exception.UserAlreadyExistedException;
import taskflower.taskflower.model.dto.UserDto;

import java.util.List;
import java.util.Random;

@SpringBootTest
class TagServiceTest {

    private final TagService tagService;
    private final UserService userService;

    @Autowired
    TagServiceTest(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    private User user;

    @BeforeEach
    void user() throws UserAlreadyExistedException {
        String email = "test@test.test";
        if (userService.existsUser(email)) {
            UserDto existedUser = userService.findUserByEmail(email);
            user = userService.getUserById(existedUser.getId());
        } else {
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setName("test");
            signupRequest.setEmail(email);
            signupRequest.setPassword("1234");
            UserDto saveUser = userService.signup(new User(signupRequest));
            user = userService.getUserById(saveUser.getId());
        }
    }

    @Test
    @DisplayName("Tag 저장 테스트")
    void saveTest() throws TagExistException, TagNotFoundException {
        TagDto savedTag = getSavedTag();

        TagDto result = tagService.getTagById(savedTag.getId());

        Assertions.assertEquals(result, savedTag);
    }


    @Test
    @DisplayName("Tag 조회 테스트")
    void getTagsByUser() throws TagExistException {
        TagDto savedTag = getSavedTag();

        List<TagDto> tags = tagService.findAllByUserEmail(user.getEmail());

        Assertions.assertEquals(savedTag, tags.get(tags.size() - 1));
    }

    @Test
    @DisplayName("Tag 수정 테스트")
    void updateTag() throws TagNotFoundException, TagExistException {
        TagDto tagDto = getSavedTag();
        tagDto.setName(getRandomTagName());

        TagDto updatedTag = tagService.update(tagDto);

        Assertions.assertEquals(tagDto, updatedTag);

    }

    @Test
    @DisplayName("Tag 삭제 테스트")
    void deleteTag() throws TagExistException {
        TagDto savedTag = getSavedTag();

        tagService.deleteById(savedTag.getId());

        Assertions.assertThrows(TagNotFoundException.class, () -> tagService.getTagById(savedTag.getId()));
    }

    @Test
    @DisplayName("중복 태그 생성 시 예외 발생 테스트")
    void duplicateTagNameTest() throws TagExistException {
        TagDto savedTag = getSavedTag();

        TagDto duplicateTag = new TagDto();
        duplicateTag.setName(savedTag.getName());

        Assertions.assertThrows(TagExistException.class, () -> tagService.save(duplicateTag, user));
    }

    private String getRandomTagName() {
        Random random = new Random();
        StringBuilder tagName = new StringBuilder();

        tagName.append((char) (random.nextInt(26) + 'a'));

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 1; i < 10; i++) {
            tagName.append(characters.charAt(random.nextInt(characters.length())));
        }

        return tagName.toString();
    }

    private TagDto getSavedTag() throws TagExistException {
        TagDto tagDto = new TagDto();
        tagDto.setName(getRandomTagName());

        try {
            return tagService.save(tagDto, user);
        } catch (TagExistException e) {
            tagDto.setName(getRandomTagName());
            return tagService.save(tagDto, user);
        }
    }
}