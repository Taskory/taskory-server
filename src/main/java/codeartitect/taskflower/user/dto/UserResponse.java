package codeartitect.taskflower.user.dto;

import codeartitect.taskflower.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse {
    private Long id;
    private String username;
    private String zoneId;
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.zoneId = user.getZoneId();
    }
}
