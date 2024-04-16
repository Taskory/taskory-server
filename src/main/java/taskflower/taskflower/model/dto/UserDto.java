package taskflower.taskflower.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import taskflower.taskflower.model.entity.SocialAccount;

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
    @JsonIgnore
    private Set<SocialAccount> socialAccounts;
}
