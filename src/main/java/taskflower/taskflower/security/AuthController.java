package taskflower.taskflower.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflower.taskflower.user.User;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequset loginRequset) {
        User user = new User();
        user.setName("test");
        user.setEmail("test@test.test");
        user.setPassword("1234");
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/login")
    public ResponseEntity<User> temp() {
        User user = new User();
        user.setName("test");
        user.setEmail("test@test.test");
        user.setPassword("1234");
        return ResponseEntity.ok().body(user);
    }

}
