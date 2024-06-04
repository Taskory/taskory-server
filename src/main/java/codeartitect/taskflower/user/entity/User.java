package codeartitect.taskflower.user.entity;

import codeartitect.taskflower.user.Role;
import codeartitect.taskflower.user.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public User(UserSignupRequest userSignupRequest) {
        this.username = userSignupRequest.getUsername();
        this.password = userSignupRequest.getPassword();
        this.zoneId = userSignupRequest.getZoneId();
        this.roles.add(Role.USER);

    }

}
