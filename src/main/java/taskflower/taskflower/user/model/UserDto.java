package taskflower.taskflower.user.model;

import lombok.*;
import taskflower.taskflower.user.SocialAccount.SocialAccount;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Set<SocialAccount> socialAccount;
}
