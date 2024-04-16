package taskflower.taskflower.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.security.CurrentUser;
import taskflower.taskflower.security.UserPrincipal;
import taskflower.taskflower.user.model.ProfileResponse;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

@RestController
@RequestMapping("${app.api-base-url}/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        ProfileResponse profile = userService.getProfile(userPrincipal.getId());
        return ResponseEntity.ok().body(profile);
    }
}
