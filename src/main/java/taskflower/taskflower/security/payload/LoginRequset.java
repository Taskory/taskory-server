package taskflower.taskflower.security.payload;

import lombok.Data;

@Data
public class LoginRequset {
    private String email;
    private String password;
}
