package taskflower.taskflower.security.data.dto;

import lombok.Data;

@Data
public class LoginRequset {
    private String email;
    private String password;
}
