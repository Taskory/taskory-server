package codeartist99.taskflower.tag;

import codeartist99.taskflower.event.Event;
import codeartist99.taskflower.event.EventRepository;
import codeartist99.taskflower.tag.model.Color;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.tag.payload.SaveTagRequest;
import codeartist99.taskflower.tag.payload.TagResponse;
import codeartist99.taskflower.task.model.Task;
import codeartist99.taskflower.task.repository.TaskRepository;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TagService(TagRepository tagRepository, TaskRepository taskRepository, EventRepository eventRepository) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Save tag
     * @param user User information
     * @param saveTagRequest Information to save tag
     * @return TagResponse
     */
    public TagResponse save(User user, SaveTagRequest saveTagRequest) {
        if (saveTagRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (!ColorValidator.isValidColor(saveTagRequest.getColor())) {
            throw new IllegalArgumentException("Color cannot be empty");
        }
        Tag tag = Tag.builder()
                .user(user)
                .title(saveTagRequest.getTitle())
                .color(Color.valueOf(saveTagRequest.getColor()))
                .build();

        tagRepository.save(tag);

        return new TagResponse(tag);
    }

    /**
     * Get tag by tag id
     * @param id tag id
     * @return TagResponse
     */
    public TagResponse getById(Long id) throws TagNotFoundException {
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
     * @param saveTagRequest Information to update tag
     * @return TagResponse
     */
    public TagResponse updateTag(Long tagId, SaveTagRequest saveTagRequest) throws TagNotFoundException {
        Tag tag = tagRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
        tag.update(saveTagRequest);

        Tag updateTag = tagRepository.save(tag);
        return new TagResponse(updateTag);
    }

    /**
     * Delete tag by tag id
     * @param id Tag id for delete
     */
    public void deleteById(Long id) throws TagNotFoundException {
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotFoundException::new);

        List<Task> tasks = taskRepository.findByTag(tag);
        if (!tasks.isEmpty()) {
            taskRepository.deleteAll(tasks);
        }

        List<Event> events = eventRepository.findByTag(tag);
        if (!events.isEmpty()) {
            eventRepository.deleteAll(events);
        }

        tagRepository.delete(tag);
    }
}
