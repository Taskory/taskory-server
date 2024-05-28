package codeartitect.taskflower.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignupRequest {
    private String username;
    private String password;
    private String zoneId;
}
