package codeartitect.taskflower.user.entity;

import codeartitect.taskflower.user.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String zoneId;
    @OneToMany
    private Set<SocialAccount> socialAccounts;

    public User(UserSignupRequest userSignupRequest) {
        this.username = userSignupRequest.getUsername();
        this.password = userSignupRequest.getPassword();
        this.zoneId = userSignupRequest.getZoneId();
    }

}
