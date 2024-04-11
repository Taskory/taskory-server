package taskflower.taskflower.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import taskflower.taskflower.security.payload.SignupRequset;
import taskflower.taskflower.global.BaseTimeEntity;
import taskflower.taskflower.user.social.SocialAccount;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends BaseTimeEntity {

    public User(SignupRequset signupRequset) {
        this.name = signupRequset.getName();
        this.email = signupRequset.getEmail();
        this.password = signupRequset.getPassword();
    }

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

    @OneToMany(fetch = FetchType.EAGER)
    private Set<SocialAccount> socialAccount;

//    oAuth2로 로그인하여 임시 로컬 계정으로 등록이 되어 있을 때 false로 수정
    @Column(nullable = false)
    private boolean isLocalSignup = true;
}

