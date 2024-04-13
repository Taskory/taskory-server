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
import taskflower.taskflower.security.TokenService;
import taskflower.taskflower.security.payload.LoginRequset;
import taskflower.taskflower.security.payload.LoginResponse;
import taskflower.taskflower.security.payload.SignupRequest;
import taskflower.taskflower.security.payload.SignupResponse;
import taskflower.taskflower.user.model.User;
import taskflower.taskflower.user.exception.UserAlreadyExistedException;
import taskflower.taskflower.user.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
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

        String token = tokenService.createToken(authentication);

        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            User user = new User(signupRequest);
            userService.signup(user);
            return ResponseEntity.ok().body(new SignupResponse("회원가입 성공"));
        } catch (UserAlreadyExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignupResponse("이미 존재하는 사용자입니다."));
        }
    }
}
