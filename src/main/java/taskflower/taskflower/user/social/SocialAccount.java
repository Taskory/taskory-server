package taskflower.taskflower.user.social;

import jakarta.persistence.*;
import lombok.*;
import taskflower.taskflower.global.BaseTimeEntity;
import taskflower.taskflower.user.model.User;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Primary id of OAuth2 provider
    @Column
    private String subId;

    @ManyToOne
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

//    user ID or Email of OAuth2 provider
    @Column
    private String username;

    @Column
    private String name;

}
