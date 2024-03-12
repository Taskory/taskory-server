package taskflower.taskflower.task.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserService userService;

    @Autowired
    public TagService(TagRepository tagRepository, UserService userService) {
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public TagDto save(TagDto tagDto, User user) {
        Tag tag = new Tag();
        tag.setUser(user);
        tag.setName(tagDto.getName());

        Tag savedTag = tagRepository.save(tag);

        TagDto result = new TagDto();
        result.setName(savedTag.getName());

        return result;

    }

    public TagDto getTagById(Long id) throws TagNotFoundException {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));

        TagDto result = new TagDto();
        result.setName(tag.getName());
        return result;
    }

    public Set<TagDto> findAllByUserEmail(String email) {
        try {
            User user = userService.findUserByEmail(email);
            Set<Tag> tags = tagRepository.findAllByUser(user);

            Set<TagDto> result = new HashSet<>();
            for (Tag tag : tags) {
                TagDto tagDto = new TagDto();
                tagDto.setName(tag.getName());
                result.add(tagDto);
            }

            return result;
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("User Email Not Found");
        }
    }

    public TagDto update(Long id, TagDto tagDto) throws TagNotFoundException {
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
        tag.setName(tagDto.getName());

        Tag updatedTag = tagRepository.save(tag);

        TagDto result = new TagDto();
        result.setName(updatedTag.getName());

        return result;
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

}
