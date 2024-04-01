package taskflower.taskflower.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import taskflower.taskflower.auth.payload.SignupRequset;
import taskflower.taskflower.global.BaseTimeEntity;

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

}

