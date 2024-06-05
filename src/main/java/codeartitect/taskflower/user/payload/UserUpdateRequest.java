package codeartitect.taskflower.user.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private Long id;
    private String username;
    private String password;
    private String zoneId;
}
