package taskflower.taskflower.security.local;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.security.TokenProvider;
import taskflower.taskflower.security.payload.LoginRequset;
import taskflower.taskflower.security.payload.LoginResponse;
import taskflower.taskflower.security.payload.SignupRequset;
import taskflower.taskflower.security.payload.SignupResponse;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(TokenProvider tokenProvider, UserService userService, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequset loginRequset) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequset.getEmail(),
                        loginRequset.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequset signupRequset) throws UserAlreadyExistedException {
        try {
            User user = new User(signupRequset);
            userService.signup(user);
            return ResponseEntity.ok().body(new SignupResponse("회원가입 성공"));
        } catch (UserAlreadyExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignupResponse("이미 존재하는 사용자입니다."));
        }
    }
}
