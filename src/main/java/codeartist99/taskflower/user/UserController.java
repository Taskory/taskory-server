package codeartist99.taskflower.user;

import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.payload.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.url-base}/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserResponse getProfile(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getByUsername(userPrincipal.getUsername());
    }

}
