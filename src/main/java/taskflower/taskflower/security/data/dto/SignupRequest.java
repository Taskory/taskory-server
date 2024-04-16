package taskflower.taskflower.security.data.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private Long id;
    private String name;
    private String email;
    private String password;

}
