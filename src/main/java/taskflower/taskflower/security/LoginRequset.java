package taskflower.taskflower.security;

import lombok.Data;

@Data
public class LoginRequset {
    private String email;
    private String password;
}
