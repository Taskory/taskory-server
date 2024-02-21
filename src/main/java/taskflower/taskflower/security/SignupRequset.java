package taskflower.taskflower.security;

import lombok.Data;

@Data
public class SignupRequset {
    private String name;
    private String email;
    private String password;

}
