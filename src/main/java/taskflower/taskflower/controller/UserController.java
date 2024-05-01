package taskflower.taskflower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.annotation.CurrentUser;
import taskflower.taskflower.security.model.UserPrincipal;
import taskflower.taskflower.service.UserService;
import taskflower.taskflower.payload.ProfileResponse;

@RestController
@RequestMapping("${app.api-base-url}/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        try {
            ProfileResponse profile = userService.getProfile(userPrincipal.getId());
            return ResponseEntity.ok().body(profile);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.toString());
        }
    }
}
