package codeartitect.taskflower.Tag;

import codeartitect.taskflower.Tag.model.Tag;
import codeartitect.taskflower.Tag.payload.SaveTagRequest;
import codeartitect.taskflower.Tag.payload.TagResponse;
import codeartitect.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Save tag
     * @param user User information
     * @param saveTagRequest Information to save tag
     * @return TagResponse
     */
    public TagResponse save(User user, SaveTagRequest saveTagRequest) {
        Tag tag = new Tag(user, saveTagRequest);

        tagRepository.save(tag);

        return new TagResponse(tag);
    }

    /**
     * Get tag by tag id
     * @param id tag id
     * @return TagResponse
     */
    public TagResponse getById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
        return new TagResponse(tag);
    }

    /**
     * Find all tags by user info
     * @param user User information
     * @return TagResponse list
     */
    public List<TagResponse> findAll(User user) {
        List<Tag> tags = tagRepository.findAllByUser(user);

        List<TagResponse> tagResponseList = new ArrayList<>();
        for (Tag tag : tags) {
            tagResponseList.add(new TagResponse(tag));
        }
        return tagResponseList;
    }

    /**
     * Update tag
     * @param tagId tag id
     * @param saveTagRequest Information to update flow
     * @return TagResponse
     */
    public TagResponse updateTag(Long tagId, SaveTagRequest saveTagRequest) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
        tag.update(saveTagRequest);

        Tag updateTag = tagRepository.save(tag);
        return new TagResponse(updateTag);
    }

    /**
     * Delete tag by tag id
     * @param id Tag id for delete
     */
    public void deleteById(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else throw new TagNotFoundException();
    }
}
