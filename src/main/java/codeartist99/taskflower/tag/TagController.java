package codeartist99.taskflower.tag;

import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.tag.payload.SaveTagRequest;
import codeartist99.taskflower.tag.payload.TagResponse;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.url-base}/tag")
public class TagController {

    private final TagService tagService;
    private final UserRepository userRepository;

    @Autowired
    public TagController(TagService tagService, UserRepository userRepository) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    /**
     * Save a new tag
     * @param userPrincipal The authenticated user
     * @param saveTagRequest The request payload to save a new tag
     * @return The created TagResponse
     */
    @PostMapping
    public ResponseEntity<TagResponse> saveTag(@CurrentUser UserPrincipal userPrincipal,
                                               @RequestBody SaveTagRequest saveTagRequest) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        log.info("[LOG] saveTagRequest.title: {}", saveTagRequest.getTitle());
        log.info("[LOG] saveTagRequest.color: {}", saveTagRequest.getColor());
        TagResponse tagResponse = tagService.save(user, saveTagRequest);
        return ResponseEntity.ok(tagResponse);
    }

    /**
     * Get tag by ID
     * @param id The ID of the tag
     * @return The TagResponse with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
        try {
            TagResponse tagResponse = tagService.getById(id);
            return ResponseEntity.ok(tagResponse);
        } catch (TagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all tags for the authenticated user
     * @param userPrincipal The authenticated user
     * @return A list of TagResponse
     */
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        List<TagResponse> tagResponses = tagService.findAll(user);
        return ResponseEntity.ok(tagResponses);
    }

    /**
     * Update a tag by ID
     * @param id The ID of the tag to update
     * @param saveTagRequest The request payload to update the tag
     * @return The updated TagResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long id,
                                                 @RequestBody SaveTagRequest saveTagRequest) {
        try {
            TagResponse tagResponse = tagService.updateTag(id, saveTagRequest);
            return ResponseEntity.ok(tagResponse);
        } catch (TagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a tag by ID
     * @param id The ID of the tag to delete
     * @return A response indicating the result of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagById(@PathVariable("id") Long id) {
        try {
            tagService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (TagNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
