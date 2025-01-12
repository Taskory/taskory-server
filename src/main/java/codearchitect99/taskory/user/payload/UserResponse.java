package codearchitect99.taskory.user.payload;

import codearchitect99.taskory.user.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse {
    private Long id;
    private String username;
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
