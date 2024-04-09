package taskflower.taskflower.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.security.CurrentUser;
import taskflower.taskflower.security.UserPrincipal;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.model.UserDto;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@CurrentUser UserPrincipal userDetails) {
        UserDto user = userService.findUserByEmail(userDetails.getEmail());
        return ResponseEntity.ok().body(user);
    }

}
