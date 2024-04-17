package taskflower.taskflower.security.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.security.data.UserPrincipal;
import taskflower.taskflower.security.data.dto.LoginRequset;
import taskflower.taskflower.security.data.dto.LoginResponse;
import taskflower.taskflower.security.data.dto.SignupRequest;
import taskflower.taskflower.security.data.dto.SignupResponse;
import taskflower.taskflower.security.service.CustomUserDetailsService;
import taskflower.taskflower.security.service.TokenService;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.exception.UserAlreadyExistedException;
import taskflower.taskflower.service.UserService;

@RestController
@RequestMapping("${app.api-base-url}/auth")
public class AuthenticationController {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequset loginRequset) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequset.getEmail(), loginRequset.getPassword()));

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

    /*
    * OAuth2로 로그인 후 임시 계정을 로컬 계정으로 가입
    * User의 Role이 수정되기 때문에 Authentication Update 필요
    * */
    @PostMapping("/oauth2/signup")
    public ResponseEntity<SignupResponse> signupWithOAuth2(@Valid @RequestBody SignupRequest signupRequest) {
        User user = new User(signupRequest);
        userService.signupWithOAuth2(user);

        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserById(user.getId());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return ResponseEntity.ok().body(new SignupResponse("회원가입 성공"));
    }
}
