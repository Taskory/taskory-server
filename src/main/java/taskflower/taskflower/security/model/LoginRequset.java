package taskflower.taskflower.security.model;

import lombok.Data;

@Data
public class LoginRequset {
    private String email;
    private String password;
}
