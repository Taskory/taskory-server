package taskflower.taskflower.security.payload;

import lombok.Data;

@Data
public class SignupRequset {
    private String name;
    private String email;
    private String password;

}
