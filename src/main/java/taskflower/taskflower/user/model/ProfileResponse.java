package taskflower.taskflower.user.model;

import lombok.*;
import taskflower.taskflower.user.social.SocialAccount;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProfileResponse {
    private String name;
    private String email;
    private Set<String> socialProviders = new HashSet<>();
}
