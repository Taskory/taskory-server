package codeartitect.taskflower.security.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Deprecated
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
