package taskflower.taskflower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.annotation.CurrentUser;
import taskflower.taskflower.security.model.UserPrincipal;
import taskflower.taskflower.service.TagService;
import taskflower.taskflower.exception.TagExistException;
import taskflower.taskflower.exception.TagNotFoundException;
import taskflower.taskflower.model.dto.TagDto;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.service.UserService;

import java.util.List;

@RestController
@RequestMapping("${app.api-base-url}/task/tag")
public class TagController {

    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public TagController(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> findAllTags(@CurrentUser UserPrincipal userDetails) {
        List<TagDto> tags = tagService.findAllByUserEmail(userDetails.getEmail());
        return ResponseEntity.ok().body(tags);
    }

    @PostMapping
    public ResponseEntity<Object> save(@CurrentUser UserPrincipal userDetails, @RequestBody TagDto tagDto) {
        User user = userService.getUserById(userDetails.getId());
        try {
            TagDto savedTag = tagService.save(tagDto, user);
            return ResponseEntity.ok().body(savedTag);
        } catch (TagExistException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody TagDto tagDto) {
        try {
            TagDto result = tagService.update(tagDto);
            return ResponseEntity.ok().body(result);
        } catch (TagNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.ok().body("The Tag is successfully deleted ");
    }
}
