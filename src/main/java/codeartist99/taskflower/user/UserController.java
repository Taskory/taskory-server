package codeartist99.taskflower.user;

import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.exception.InvalidZoneIdException;
import codeartist99.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartist99.taskflower.user.payload.ProfileUpdateRequest;
import codeartist99.taskflower.user.payload.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${app.url-base}/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@CurrentUser UserPrincipal userPrincipal) {
        UserResponse response = userService.getByUsername(userPrincipal.getUsername());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username) {
        log.info("[LOG - UserController.checkUsername] Checking username:  {}", username);
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok().body(isAvailable);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@CurrentUser UserPrincipal userPrincipal, @RequestBody ProfileUpdateRequest request) {
        try {
            UserResponse response = userService.updateProfile(userPrincipal.getId(), request);
            return ResponseEntity.ok().body(response);
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (InvalidZoneIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/zoneid")
    public ResponseEntity<String> getZoneId(@CurrentUser UserPrincipal userPrincipal) {
        String response = userService.getByUsername(userPrincipal.getUsername()).getTimezone();
        return ResponseEntity.ok().body(response);
    }
}
