package taskflower.taskflower.task.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.BadRequestResponse;
import taskflower.taskflower.security.CurrentUser;
import taskflower.taskflower.security.UserDetailsImpl;
import taskflower.taskflower.user.User;
import taskflower.taskflower.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task/tag")
public class TagController {

    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public TagController(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> findAllTags(@CurrentUser UserDetailsImpl userDetails) {
        List<TagDto> tags = tagService.findAllByUserEmail(userDetails.getEmail());
        return ResponseEntity.ok().body(tags);
    }

    @PostMapping
    public ResponseEntity<?> save(@CurrentUser UserDetailsImpl userDetails, @RequestBody TagDto tagDto) {
        User user = userService.getUserById(userDetails.getId());
        try {
            TagDto savedTag = tagService.save(tagDto, user);
            return ResponseEntity.ok().body(savedTag);
        } catch (TagExistException exception) {
            return ResponseEntity.badRequest().body(new BadRequestResponse(exception.getMessage()));
        }
    }
}
