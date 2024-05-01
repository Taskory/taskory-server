package taskflower.taskflower.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import taskflower.taskflower.model.enums.Role;
import taskflower.taskflower.payload.SignupRequest;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<SocialAccount> socialAccounts = new HashSet<>();

    //    oAuth2로 로그인하여 임시 로컬 계정으로 등록이 되어 있을 때 false로 수정
//    @Column(nullable = false)
//    private boolean isLocalSignup = true;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    /* 로컬 회원 가입할 때 사용*/
    public User(SignupRequest signupRequest) {
        this.name = signupRequest.getName();
        this.email = signupRequest.getEmail();
        this.password = signupRequest.getPassword();
        roles.add(Role.USER);
    }

    public User(SignupRequest signupRequest, Long id) {
        this.id = id;
        this.name = signupRequest.getName();
        this.email = signupRequest.getEmail();
        this.password = signupRequest.getPassword();
        roles.add(Role.USER);

    }

    public void addSocialAccount(SocialAccount socialAccount) {
        this.socialAccounts.add(socialAccount);
        socialAccount.setUser(this);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void initRole() {
        this.roles = new HashSet<>();
    }
}

