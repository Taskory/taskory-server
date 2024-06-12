package codeartitect.taskflower.user.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Deprecated
public class SignupRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String zoneId;
}
