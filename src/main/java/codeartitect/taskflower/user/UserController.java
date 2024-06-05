package codeartitect.taskflower.user;

import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.payload.SignupResponse;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.url-base}/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            UserResponse userResponse = userService.signup(signupRequest);
            SignupResponse signupResponse = new SignupResponse(userResponse);
            return ResponseEntity.ok().body(signupResponse);
        } catch (InvalidZoneIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignupResponse("Invalid Zone id."));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SignupResponse("Username already exists."));
        }
    }

}
