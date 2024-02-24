package taskflower.taskflower.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import taskflower.taskflower.security.model.SignupRequset;

@Entity
//@Table(
//        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class User {
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

    public User(SignupRequset signupRequset) {
        this.name = signupRequset.getName();
        this.email = signupRequset.getEmail();
        this.password = signupRequset.getPassword();
    }

    public User() {

    }
}
