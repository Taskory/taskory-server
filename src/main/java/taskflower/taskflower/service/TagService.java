package taskflower.taskflower.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskflower.taskflower.mapper.TagMapper;
import taskflower.taskflower.repository.TagRepository;
import taskflower.taskflower.exception.TagExistException;
import taskflower.taskflower.exception.TagNotFoundException;
import taskflower.taskflower.model.entity.Tag;
import taskflower.taskflower.model.dto.TagDto;
import taskflower.taskflower.repository.UserRepository;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.exception.UserNotFoundException;
import taskflower.taskflower.model.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserService userService;
    private final TagMapper tagMapper;
    private final UserRepository userRepository;

    @Autowired
    public TagService(TagRepository tagRepository, UserService userService, TagMapper tagMapper, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.userRepository = userRepository;
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
            UserDto userDto = userService.findUserByEmail(email);
            User user = userRepository.findById(userDto.getId())
                    .orElseThrow(UserNotFoundException::new);
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
