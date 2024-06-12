package codeartitect.taskflower.user.model;

import codeartitect.taskflower.user.payload.ProfileUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "zone_id", nullable = false)
    private String zoneId;
    @OneToMany
    private Set<SocialAccount> socialAccounts;
    @Column(name = "roles", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        this.username = profileUpdateRequest.getUsername();
        this.zoneId = profileUpdateRequest.getZoneId();
    }
}
