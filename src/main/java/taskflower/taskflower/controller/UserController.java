package taskflower.taskflower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.annotation.CurrentUser;
import taskflower.taskflower.security.data.UserPrincipal;
import taskflower.taskflower.service.UserService;
import taskflower.taskflower.model.dto.ProfileResponse;

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
