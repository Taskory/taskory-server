package taskflower.taskflower.security.model;

import lombok.Data;

@Data
public class SignupRequset {
    private String name;
    private String email;
    private String password;

}
