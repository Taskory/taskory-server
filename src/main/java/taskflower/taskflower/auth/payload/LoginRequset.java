package taskflower.taskflower.auth.payload;

import lombok.Data;

@Data
public class LoginRequset {
    private String email;
    private String password;
}
