package taskflower.taskflower.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import taskflower.taskflower.security.oauth2.AuthProviderType;

@Entity
@Data
public class OAuth2ConnectionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthProviderType providerType;

    @Column(nullable = false)
    private String username;

}
