package taskflower.taskflower.task.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskflower.taskflower.task.tag.exception.TagExistException;
import taskflower.taskflower.task.tag.exception.TagNotFoundException;
import taskflower.taskflower.task.tag.model.Tag;
import taskflower.taskflower.task.tag.model.TagDto;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;
import taskflower.taskflower.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserService userService;
    private final TagMapper tagMapper;

    @Autowired
    public TagService(TagRepository tagRepository, UserService userService, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.tagMapper = tagMapper;
    }

    public TagDto save(TagDto tagDto, User user) throws TagExistException {
        if (tagRepository.existsByNameAndUser(tagDto.getName(), user)) {
            throw new TagExistException("Tag already exists");
        }
        Tag tag = tagMapper.convertTagDtoToTag(tagDto);
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);

        return tagMapper.converTagToTagDto(savedTag);
    }

    public TagDto getTagById(Long id) throws TagNotFoundException {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));

        return tagMapper.converTagToTagDto(tag);
    }

    public List<TagDto> findAllByUserEmail(String email) {
        try {
            User user = userService.findUserByEmail(email);
            List<Tag> tags = tagRepository.findAllByUser(user);

            List<TagDto> result = new ArrayList<>();

            for (Tag tag : tags) {
                TagDto tagDto = tagMapper.converTagToTagDto(tag);
                result.add(tagDto);
            }

            return result;
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException("User Email Not Found");
        }
    }

    public TagDto update(TagDto tagDto) throws TagNotFoundException {
        Tag tag = tagRepository.findById(tagDto.getId()).orElseThrow(TagNotFoundException::new);
        tag.setName(tagDto.getName());

        Tag updatedTag = tagRepository.save(tag);

        return tagMapper.converTagToTagDto(updatedTag);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

}
