package taskflower.taskflower.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskflower.taskflower.security.payload.SignupRequset;

@Entity
@Data
@NoArgsConstructor
public class User {

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
