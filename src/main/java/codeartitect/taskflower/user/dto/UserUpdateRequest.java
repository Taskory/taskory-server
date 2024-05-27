package codeartitect.taskflower.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private Long id;
    private String username;
    private String password;
    private String zoneId;
}
