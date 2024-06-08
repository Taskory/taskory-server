package codeartitect.taskflower.user.entity;

import codeartitect.taskflower.user.Role;
import codeartitect.taskflower.user.payload.ProfileUpdateRequest;
import codeartitect.taskflower.user.payload.SignupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "zone_id", nullable = false)
    private String zoneId;
    @OneToMany
    private Set<SocialAccount> socialAccounts;
    @Column(name = "roles", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public User(SignupRequest signupRequest) {
        this.username = signupRequest.getUsername();
        this.password = signupRequest.getPassword();
        this.zoneId = signupRequest.getZoneId();
        this.roles.add(Role.USER);

    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        this.username = profileUpdateRequest.getUsername();
        this.zoneId = profileUpdateRequest.getZoneId();
    }
}
